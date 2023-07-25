package ru.kosti.dispatcher.user.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.kosti.ResponseType;
import ru.kosti.dispatcher.user.proxy.NodeProxy;
import ru.kosti.dispatcher.user.service.ConsumerCallbackService;
import ru.kosti.dispatcher.user.utils.emuns.AppUserStates;
import ru.kosti.dispatcher.user.utils.emuns.CallbackActions;
import ru.kosti.dispatcher.user.utils.emuns.UserColumn;
import ru.kosti.dispatcher.user.utils.keyboards.ChatKeyboards;
import ru.kosti.dispatcher.user.utils.keyboards.InlineKeyboards;
import ru.kosti.dispatcher.user.utils.message.Answer;

import java.io.ByteArrayInputStream;
import java.util.Map;

import static ru.kosti.dispatcher.user.utils.message.MessageUtils.*;

@Service
public class ConsumerCallbackServiceImpl implements ConsumerCallbackService {
    private final InlineKeyboards inlineKeyboards;
    private final ChatKeyboards chatKeyboards;
    private final NodeProxy proxy;
    private final String BASE = AppUserStates.BASIC_STATE.toString();
    private final String SEND = AppUserStates.SEND_RESULTS_STATE.toString();
    private final String FIND = AppUserStates.FIND_STATE.toString();

    public ConsumerCallbackServiceImpl(InlineKeyboards inlineKeyboards, ChatKeyboards chatKeyboards, NodeProxy proxy) {
        this.inlineKeyboards = inlineKeyboards;
        this.chatKeyboards = chatKeyboards;
        this.proxy = proxy;
    }

    @Override
    public Answer distributeCallbackData(Update update) {
        long telegramId = update.getCallbackQuery().getFrom().getId();
        var rawData = parseCallbackData(update.getCallbackQuery().getData());
        return distributeByAction(update, rawData, telegramId);
    }

    private Answer distributeByAction(Update update, Map<String, String> rawData, long telegramId) {
        var vector = rawData.get("vector");
        var action = CallbackActions.valueOf(rawData.get("action"));
        var answer = new Answer();

        switch (action) {
            case MINIMAL_POINTS -> {
                // Ищем минимальные баллы по направлению и изменяем состояние пользователя на базовое
                answer.setSendMessage(minimalPointsAction(update, vector, telegramId));
                proxy.updateInformation(telegramId, UserColumn.STATES, BASE);
            }
            case FIND -> {
                // Ищем позицию человека в рейтинге
                var list = convert(rawData);
                var m = findAction(update, vector, list, telegramId);
                m.setReplyMarkup(inlineKeyboards.webAppKeyboard(vector, list));
                answer.setSendMessage(m);
                proxy.updateInformation(telegramId, UserColumn.STATES, SEND);
            }
            case CHOOSE_LIST -> {
                answer.setSendMessage(createMessage(update, "Выберите нужный список"));
                proxy.updateInformation(telegramId, UserColumn.STATES, FIND);
            }
            case WAIT_SCV -> {
                var list = convert(rawData);
                answer.setSendDocument(getFile(telegramId, vector, "csv", list));
                proxy.updateInformation(telegramId, UserColumn.STATES, BASE);

            }
            case WAIT_HTML -> {
                var list = convert(rawData);
                answer.setSendDocument(getFile(telegramId, vector, "html", list));
                proxy.updateInformation(telegramId, UserColumn.STATES, BASE);

            }
            case CANSEL -> {
                answer.setSendMessage(createMessage(update, "Окей!"));
                proxy.updateInformation(telegramId, UserColumn.STATES, BASE);
            }
            default -> {
                answer.setSendMessage(createMessage(update, "Я не понимаю тебя"));
                proxy.updateInformation(telegramId, UserColumn.STATES, BASE);
            }
        }
        var state = proxy.getUserColumn(telegramId, UserColumn.STATES).getBody();
        return addKeyboardByState(vector, answer, AppUserStates.valueOf(state));
    }

    private SendMessage findAction(Update update, String vector, String list, long telegramId) {
        var snils = proxy.getUserColumn(telegramId, UserColumn.SNILS).getBody() == null ? "0" : proxy.getUserColumn(telegramId, UserColumn.SNILS).getBody();
        var response = getBody(proxy.getApplicantInformation(vector, snils, list, telegramId));
        if (response == null || !response.hasApplicantData() || !response.hasVectorData()) return createMessage(update, "Что-то пошло не так");
        var applicantData = response.getApplicantData();
        var vectorData = response.getVectorData();
        StringBuilder text;
        text = new StringBuilder();
        int places = switch (list) {
            case "budget" -> vectorData.getBudgetPlaces();
            case "contract" -> vectorData.getContractPlaces();
            case "special" -> vectorData.getSpecialPlaces();
            case "separate" -> vectorData.getSeparatePlaces();
            case "contractAbroad" -> vectorData.getContractAbroadPlaces();
            default -> -1;
        };
        text.append("Данные актуальны на: ")
                .append(applicantData.getActuality_date())
                .append("\nВаша позиция на направление ")
                .append(vector).append(" равна: ")
                .append(applicantData.getPosition())
                .append("\nМест на этом направлении в выбраном списке: ")
                .append(places);
        return createMessage(update, text.toString());
    }

    private SendMessage minimalPointsAction(Update update, String vector, long telegramId) {
        var response = getBody(proxy.getVectorData(vector, telegramId));
        if (response == null || !response.hasVectorData()) return createMessage(update, "Что-то не так!");
        var vcrInf = response.getVectorData();
        StringBuilder text;
        text = new StringBuilder();
        text.append("\nВ прошлом году минимальные проходные баллы на направление ")
                .append(vector).append(" были:\nБюджет: ").append(vcrInf.getMinimalPointsBudget().getOneYearAgo())
                .append("\n2 года назад минимальные баллы были:\nБюджет: ")
                .append(vcrInf.getMinimalPointsBudget().getTwoYearAgo())
                .append("\n3 года назад минимальные баллы были:\nБюджет: ")
                .append(vcrInf.getMinimalPointsBudget().getTreeYearAgo());
        return createMessage(update, text.toString());
    }

    private SendDocument getFile(long telegramId, String vector, String type, String type_of_list) {
        var document = new SendDocument();
        document.setChatId(telegramId);
        byte[] file;
        ResponseEntity<ResponseType> response;
        if (type.equals("csv")) response = proxy.getCsvFile(vector, type_of_list, telegramId);
        else response = proxy.getHtmlFile(vector, type_of_list, telegramId);
        if (response.getStatusCode() != HttpStatus.OK || !response.hasBody() || response.getBody() == null
                || !response.getBody().hasFile()) return document;
        file = response.getBody().getFile();
        var stream = new ByteArrayInputStream(file);
        var inputFile = new InputFile(stream, vector + '.' + type);
        document.setDocument(inputFile);
        return document;
    }

    private ResponseType getBody(ResponseEntity<ResponseType> response) {
        if (response.getStatusCode() != HttpStatus.OK) return null;
        if (!response.hasBody() || response.getBody() == null) return null;
        return response.getBody();
    }

    private Answer addKeyboardByState(String vector, Answer answer, AppUserStates state) {
        switch (state) {
            case BASIC_STATE -> {
                if (answer.hasMessage()) {
                    var k = chatKeyboards.getBaseChatKeyboard();
                    var m = answer.getSendMessage();
                    m.setReplyMarkup(k);
                    answer.setSendMessage(m);
                } else if (answer.hasDoc()) {
                    var k = chatKeyboards.getBaseChatKeyboard();
                    var d = answer.getSendDocument();
                    d.setReplyMarkup(k);
                    answer.setSendDocument(d);
                }
            }
            case FIND_STATE -> {
                var k = inlineKeyboards.getChooseListForParseKeyboard(vector);
                var m = answer.getSendMessage();
                m.setReplyMarkup(k);
                answer.setSendMessage(m);
            }
        }
        return answer;
    }

    private String convert(Map<String, String> rawData) {
        return switch (rawData.get("list")) {
            case "Контракт" -> "contract";
            case "Контракт иностранцы" -> "contractAbroad";
            case "Особая квота" -> "special";
            case "Отдельная квота" -> "separate";
            default -> "budget";
        };
    }
}
