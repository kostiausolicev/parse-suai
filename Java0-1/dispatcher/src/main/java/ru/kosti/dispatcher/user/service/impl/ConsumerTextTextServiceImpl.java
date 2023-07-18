package ru.kosti.dispatcher.user.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.kosti.dispatcher.user.proxy.NodeProxy;
import ru.kosti.dispatcher.user.service.ConsumerTextService;
import ru.kosti.dispatcher.user.utils.emuns.AppUserStates;
import ru.kosti.dispatcher.user.utils.emuns.CallbackActions;
import ru.kosti.dispatcher.user.utils.emuns.UserColumn;
import ru.kosti.dispatcher.user.utils.keyboards.ChatKeyboards;
import ru.kosti.dispatcher.user.utils.keyboards.InlineKeyboards;
import ru.kosti.dispatcher.user.utils.message.Answer;

import static ru.kosti.dispatcher.user.utils.message.MessageUtils.*;

@Service
// TODO Сделать в этом классе присваивание клавиатур, чтобы не следить за этим в других методах
public class ConsumerTextTextServiceImpl implements ConsumerTextService {
    private final InlineKeyboards inlineKeyboards;
    private final ChatKeyboards chatKeyboards;
    private final NodeProxy proxy;
    private final String WAIT = AppUserStates.WAIT_FOR_SNILS_STATE.toString();
    private final String BASE = AppUserStates.BASIC_STATE.toString();
    private final String SEND = AppUserStates.SEND_RESULTS_STATE.toString();

    public ConsumerTextTextServiceImpl(InlineKeyboards inlineKeyboards, ChatKeyboards chatKeyboards, NodeProxy proxy) {
        this.inlineKeyboards = inlineKeyboards;
        this.chatKeyboards = chatKeyboards;
        this.proxy = proxy;
    }

    @Override
    public Answer commandStartHandler(Update update) {
        var r = new Answer();
        proxy.createUser(update.getMessage().getChatId());
        var message = createMessage(update, "Добро пожаловать!\nВыберете действие на клавиатуре");
        message.setReplyMarkup(chatKeyboards.getBaseChatKeyboard());
        r.setSendMessage(message);
        return r;
    }

    @Override
    public Answer commandFindHandler(Update update) {
        var r = new Answer();
        proxy.updateInformation(update.getMessage().getChatId(), UserColumn.STATES, WAIT);
        r.setSendMessage(chatKeyboards.getWaitForSnilsKeyboard(update));
        return r;
    }

    @Override
    public Answer commandMinimalPointsHandler(Update update) {
        var r = new Answer();
        proxy.updateInformation(update.getMessage().getChatId(), UserColumn.STATES, SEND);
        var message = createMessage(update, "Выберете направление");
        message.setReplyMarkup(inlineKeyboards.getChooseVectorKeyboard(update, CallbackActions.MINIMAL_POINTS));
        r.setSendMessage(message);
        return r;
    }

    @Override
    public Answer commandAboutHandler(Update update) {
        var r = new Answer();
        StringBuilder text;
        text = new StringBuilder();
        text.append("Этот бот был написан для поступающих в ГУАП для удобного мониторинга ")
                .append("своей позиции в конкурсных списках.\n")
                .append("Бот написан на Java 17 с использованием Spring Boot 2.5.2 и библиотеки ")
                .append("TelegramBots 6.1.0.\n")
                .append("Парсинг данных и обработка написаны на Python с использованием BeautifulSoup4\n")
                .append("Rest сервисы использую FastApi для Python и Openfeign для Java\n")
                .append("По вопросам/предложениям писать авторам ссылки ниже\n")
                .append("Спасибо за использование бота\n")
                .append("P.S. Посмотрев исходный код можно найти сообщения для необычных ответов :)");
        var message = createMessage(update, text.toString());
        message.setReplyMarkup(inlineKeyboards.getAboutKeyboard());
        r.setSendMessage(message);
        return r;
    }

    @Override
    public Answer commandHHelpHandler(Update update) {
        var r = new Answer();
        StringBuilder text;
        text = new StringBuilder();
        text.append("Тебе помощь не нужна, брат. Пользуйся клавиатурами.");
        var message = createMessage(update, text.toString());
        message.setReplyMarkup(chatKeyboards.getBaseChatKeyboard());
        r.setSendMessage(message);
        return r;
    }

    @Override
    public Answer distributeOtherTextMessages(Update update) {
        var r = new Answer();
        SendMessage message;
        AppUserStates state;
        var telegramId = update.getMessage().getChatId();
        var response = proxy.getUserColumn(telegramId, UserColumn.STATES);
        if (response.getStatusCode() != HttpStatus.OK || !response.hasBody() || response.getBody() == null) {
            proxy.updateInformation(update.getMessage().getChatId(), UserColumn.STATES, BASE);
            message = createMessage(update, "Что-то пошло не так. Попробуйте снова");
            r.setSendMessage(message);
            return r;
        }
        state = AppUserStates.valueOf(response.getBody());
        switch (state) {
            case WAIT_FOR_SNILS_STATE -> {
                var messageText = update.getMessage().getText();
                switch (messageText) {
                    case "Отмена" -> {
                        message = createMessage(update, "Хорошо, вернемся назад");
                        proxy.updateInformation(telegramId, UserColumn.STATES, BASE);
                        state = AppUserStates.valueOf(BASE);
                    }
                    case "Я посмотреть" -> {
                        message = createMessageForFind(update, telegramId);
                        proxy.updateInformation(telegramId, UserColumn.STATES, SEND);
                        proxy.updateInformation(telegramId, UserColumn.SNILS, "");
                        state = AppUserStates.valueOf(SEND);
                    }
                    default -> {
                        message = createMessageForFind(update, telegramId);
                        proxy.updateInformation(telegramId, UserColumn.STATES, SEND);
                        state = AppUserStates.valueOf(SEND);
                        // TODO Сделать аннотацию для проверки снилса
                        proxy.updateInformation(telegramId, UserColumn.SNILS, update.getMessage().getText());
                    }
                }
            }
            case SEND_RESULTS_STATE -> {
                message = createMessage(update, "Аболтус, на кнопку нажми");
                proxy.updateInformation(telegramId, UserColumn.STATES, BASE);
                state = AppUserStates.valueOf(BASE);
            }
            default -> message = createMessage(update, "По голове себе постучи, аболтус");
        }

        r.setSendMessage(addingKeyboardByState(update, message, state));
        return r;
    }

    private SendMessage addingKeyboardByState(Update update, SendMessage message, AppUserStates state) {
        switch (state) {
            case WAIT_FOR_SNILS_STATE -> message.setReplyMarkup(chatKeyboards.getWaitForSnilsMarkup(update));
            case SEND_RESULTS_STATE -> message.setReplyMarkup(inlineKeyboards.getChooseVectorKeyboard(update, CallbackActions.FIND));
            default -> message.setReplyMarkup(chatKeyboards.getBaseChatKeyboard());
        }
        return message;
    }

    private SendMessage createMessageForFind(Update update, long telegramId) {
        var board = inlineKeyboards.getChooseVectorKeyboard(update, CallbackActions.FIND);
        if (board == null) {
            proxy.updateInformation(telegramId, UserColumn.STATES, BASE);
            return createMessage(update, "Что-то пошло не так");
        }
        var message = createMessage(update, "Выберите направление");
        message.setReplyMarkup(board);
        return message;
    }
}
