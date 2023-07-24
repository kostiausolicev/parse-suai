package ru.kosti.dispatcher.user.controller;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.kosti.dispatcher.user.service.ConsumerTextService;
import ru.kosti.dispatcher.user.service.impl.ConsumerCallbackServiceImpl;
import ru.kosti.dispatcher.user.utils.message.Answer;

@Component
public class UpdateController {
    private TelegramBot telegramBot;
    private final ConsumerTextService consumerTextService;
    private final ConsumerCallbackServiceImpl consumerCallbackService;

    public UpdateController(ConsumerTextService consumerTextService, ConsumerCallbackServiceImpl consumerCallbackService) {
        this.consumerTextService = consumerTextService;
        this.consumerCallbackService = consumerCallbackService;
    }

    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void processUpdate(Update update) {
        setView(distribute(update));
    }

    public Answer distribute(Update update) {
        Answer answer;
        if (update.hasMessage()){
            answer = switch (update.getMessage().getText()) {
                case "/start" -> consumerTextService.commandStartHandler(update);
                case "/find", "Поиск" -> consumerTextService.commandFindHandler(update);
                case "/minimal", "Мин баллы прошлых лет" -> consumerTextService.commandMinimalPointsHandler(update);
                case "/about", "О боте" -> consumerTextService.commandAboutHandler(update);
                case "/help", "Помощь" -> consumerTextService.commandHelpHandler(update);
                default -> consumerTextService.distributeOtherTextMessages(update);
            };
        }
        else if (update.hasCallbackQuery()) {
            answer = consumerCallbackService.distributeCallbackData(update);
        }
        else {
            answer = new Answer();
        }
        return answer;
    }

    public void setView(Answer answer) {
        telegramBot.sendAnswer(answer);
    }
}
