package ru.kosti.dispatcher.user.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.kosti.dispatcher.user.utils.message.Answer;

import javax.annotation.PostConstruct;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    @Value("${bot.token}")
    private String botToken;
    @Value("${bot.name}")
    private String botName;
    private final UpdateController updateController;

    public TelegramBot(UpdateController updateController) {
        this.updateController = updateController;
    }

    @PostConstruct
    public void init() {
        updateController.registerBot(this);
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        updateController.processUpdate(update);
    }

    public void sendAnswer(Answer answer) {
        try {
            if (answer.hasMessage()) execute(answer.getSendMessage());
            else if (answer.hasDoc()) execute(answer.getSendDocument());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
