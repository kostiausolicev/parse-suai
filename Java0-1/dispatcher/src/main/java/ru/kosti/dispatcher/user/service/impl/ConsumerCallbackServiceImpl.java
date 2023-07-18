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

import static ru.kosti.dispatcher.user.utils.message.MessageUtils.createMessage;
import static ru.kosti.dispatcher.user.utils.message.MessageUtils.parseCallbackData;

@Service
public class ConsumerCallbackServiceImpl implements ConsumerCallbackService {
    private final InlineKeyboards inlineKeyboards;
    private final ChatKeyboards chatKeyboards;
    private final NodeProxy proxy;
    private final String WAIT = AppUserStates.WAIT_FOR_SNILS_STATE.toString();
    private final String BASE = AppUserStates.BASIC_STATE.toString();
    private final String SEND = AppUserStates.SEND_RESULTS_STATE.toString();

    public ConsumerCallbackServiceImpl(InlineKeyboards inlineKeyboards, ChatKeyboards chatKeyboards, NodeProxy proxy) {
        this.inlineKeyboards = inlineKeyboards;
        this.chatKeyboards = chatKeyboards;
        this.proxy = proxy;
    }

    @Override
    public Answer distributeCallbackData(Update update) {
        long telegramId = update.getCallbackQuery().getFrom().getId();
        var rawData = parseCallbackData(update.getCallbackQuery().getData());
        var vector = rawData.get("vector");
        var action = CallbackActions.valueOf(rawData.get("action"));
        SendMessage message = null;
        SendDocument document = null;
        Answer r = new Answer();

        switch (action) {
            case FIND -> {
                message = findAction(telegramId, vector, update);
                proxy.updateInformation(telegramId, UserColumn.STATES, SEND);
            }
            case MINIMAL_POINTS -> {
                message = minimalPointsAction(update, telegramId, vector);
                proxy.updateInformation(telegramId, UserColumn.STATES, BASE);
            }
            case WAIT_SCV -> {
                // TODO Сделать создание SendDocument и сделать return
//                message = createMessage(update, "По голове себе посутчи, не работает пока что SCV, аболтус");
                document = getFile(telegramId, vector, "csv");
                proxy.updateInformation(telegramId, UserColumn.STATES, BASE);
            }
            case WAIT_HTML -> {
//                message = createMessage(update, "По голове себе посутчи, не работает пока что HTML, аболтус");
                document = getFile(telegramId, vector, "html");
                proxy.updateInformation(telegramId, UserColumn.STATES, BASE);
            }
            case WAIT_WEBAPP -> {
                message = createMessage(update, "По голове себе посутчи, не работает пока что WEBAPP, аболтус");
                proxy.updateInformation(telegramId, UserColumn.STATES, BASE);
            }
            case CANSEL -> {
                message = createMessage(update, "Окей, аболтус");
                proxy.updateInformation(telegramId, UserColumn.STATES, BASE);
            }
            default -> {
                message = createMessage(update, "Еблан?");
                proxy.updateInformation(telegramId, UserColumn.STATES, BASE);
            }
        }
        var state = AppUserStates.valueOf(proxy.getUserColumn(telegramId, UserColumn.STATES).getBody());
        if (message != null) r.setSendMessage(addingKeyboardByState(update, vector, message, state));
        else r.setSendDocument(document);
        return r;
    }

//    private SendDocument sendCsvFile(Update update)

    private SendMessage addingKeyboardByState(Update update, String vector, SendMessage message, AppUserStates state) {
        switch (state) {
            case SEND_RESULTS_STATE -> message.setReplyMarkup(inlineKeyboards.webAppKeyboard(update, vector));
            case WAIT_FOR_SNILS_STATE -> message = chatKeyboards.getWaitForSnilsKeyboard(update);
            default -> message.setReplyMarkup(chatKeyboards.getBaseChatKeyboard());
        }
        return message;
    }

    private SendMessage findAction(long telegramId, String vector, Update update) {
        var snils = proxy.getUserColumn(telegramId, UserColumn.SNILS).getBody();
        var text = new StringBuilder();
        if (snils == null) snils = "0"; // Если у нас снилса по каким-то причинам нет, то мы его обнуляем
        var response = getResponseBody(telegramId, vector, snils);
        if (response == null || !response.hasApplicantData()) return createMessage(update, "Еблан?");
        var appl = response.getApplicantData();
        text.append("Данные актуальны на: ")
                .append(appl.getActuality_date())
                .append("\nВаша позиция на направление ")
                .append(vector).append(" равна: ")
                .append(appl.getPosition());
        return createMessage(update, text.toString());
    }

    private SendMessage minimalPointsAction(Update update, long telegramId, String vector) {
        var response = getResponseBody(telegramId, vector, "0");
        if (response == null || !response.hasVectorInformation()) return createMessage(update, "Еблан?");
        var vcrInf = response.getVectorData();
        StringBuilder text;
        text = new StringBuilder();
        text.append("Количество бюджетных мест: ").append(vcrInf.getBudgetPlaces())
                .append("\nКонтрактных мест: ").append(vcrInf.getContractPlaces())
                .append("\nВ прошлом году минимальные проходные баллы на направление ")
                .append(vector)
                .append(" были:\nБюджет: ")
                .append(vcrInf.getMinimalPointsBudget().getOneYearAgo()).append("\nКонтракт:")
                .append(vcrInf.getMinimalPointsContract().getOneYearAgo())
                .append("\n2 года назад минимальные баллы были:\nБюджет: ")
                .append(vcrInf.getMinimalPointsBudget().getTwoYearAgo())
                .append("\nКонтракт:").append(vcrInf.getMinimalPointsContract().getTwoYearAgo())
                .append("\n3 года назад минимальные баллы были:\nБюджет: ")
                .append(vcrInf.getMinimalPointsBudget().getTreeYearAgo())
                .append("\nКонтракт: ").append(vcrInf.getMinimalPointsContract().getTreeYearAgo());
        return createMessage(update, text.toString());
    }

    private ResponseType getResponseBody(long telegramId, String vector, String snils) {
        var response = proxy.getApplicantInformation(vector, snils, telegramId);
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            proxy.updateInformation(telegramId, UserColumn.STATES, BASE);
            return null;
        }
        return response.getBody();
    }

    private SendDocument getFile(long telegramId, String vector, String type) {
        var document = new SendDocument();
        document.setChatId(telegramId);
        byte[] file;
        ResponseEntity<ResponseType> response;
        if (type.equals("csv")) response = proxy.getCsvFile(vector, telegramId);
        else response = proxy.getHtmlFile(vector, telegramId);
        if (response.getStatusCode() != HttpStatus.OK || !response.hasBody() || response.getBody() == null
                || !response.getBody().hasFile()) return document;
        file = response.getBody().getFile();
        var stream = new ByteArrayInputStream(file);
        var inputFile = new InputFile(stream, vector + '.' + type);
        document.setDocument(inputFile);
        return document;
    }
}
