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
    private final String WAIT_S = AppUserStates.WAIT_FOR_SNILS_STATE.toString();
    private final String BASE = AppUserStates.BASIC_STATE.toString();
    private final String SEND = AppUserStates.SEND_RESULTS_STATE.toString();
    private final String WAIT_C = AppUserStates.WAIT_FOR_LIST_STATE.toString();
    private final String WAIT_V = AppUserStates.WAIT_FOR_VECTOR_STATE.toString();
    private final String FIND = AppUserStates.FIND_STATE.toString();

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
    public Answer commandHelpHandler(Update update) {
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
    public Answer commandFindHandler(Update update) {
        var r = new Answer();
        proxy.updateInformation(update.getMessage().getChatId(), UserColumn.STATES, WAIT_S);
        var message = createMessage(update, "Введите снилс или выберите соответсвующее действие на клавиатуре");
        message.setReplyMarkup(chatKeyboards.getWaitForSnilsMarkup(update));
        r.setSendMessage(message);
        return r;
    }

    @Override
    public Answer commandMinimalPointsHandler(Update update) {
        var r = new Answer();
        var telegramId = update.getMessage().getChatId();
        proxy.updateInformation(telegramId, UserColumn.STATES, WAIT_V);
        var message = createMessage(update, "Выберете направление");
        message.setReplyMarkup(inlineKeyboards.getChooseVectorKeyboard(CallbackActions.MINIMAL_POINTS));
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
                message = snilsAction(update, telegramId);
                state = AppUserStates.valueOf(proxy.getUserColumn(telegramId, UserColumn.STATES).getBody());
            }
            case BASIC_STATE -> {
                message = createMessage(update, "Я тебя не понимаю");
            }
            default -> {
                message = createMessage(update, "Выберите действие на клавитуре");
            }
        }
        r.setSendMessage(addKeyboardByState(message, state));
        return r;
    }

    private SendMessage addKeyboardByState(SendMessage message, AppUserStates state) {
        switch (state) {
            case WAIT_FOR_VECTOR_STATE -> message.setReplyMarkup(inlineKeyboards.getChooseVectorKeyboard(CallbackActions.CHOOSE_LIST));
            case BASIC_STATE -> message.setReplyMarkup(chatKeyboards.getBaseChatKeyboard());
        }
        return message;
    }

    private SendMessage snilsAction(Update update, Long telegramId) {
        var messageText = update.getMessage().getText();
        switch (messageText) {
            case "Отмена" -> {
                proxy.updateInformation(telegramId, UserColumn.STATES, BASE);
                return createMessage(update, "Хорошо, вернемся назад");
            }
            case "Я посмотреть" -> {
                proxy.updateInformation(telegramId, UserColumn.STATES, WAIT_V);
                proxy.updateInformation(telegramId, UserColumn.SNILS, "");
                return createMessageForFind(update, telegramId);
            }
            default -> {
                proxy.updateInformation(telegramId, UserColumn.STATES, WAIT_V);
                proxy.updateInformation(telegramId, UserColumn.SNILS, update.getMessage().getText());
                return createMessageForFind(update, telegramId);
            }
        }
    }

    private SendMessage createMessageForFind(Update update, long telegramId) {
        var board = inlineKeyboards.getChooseVectorKeyboard(CallbackActions.FIND);
        if (board == null) {
            proxy.updateInformation(telegramId, UserColumn.STATES, BASE);
            return createMessage(update, "Что-то пошло не так");
        }
        var message = createMessage(update, "Выберите направление");
        message.setReplyMarkup(board);
        return message;
    }
}
