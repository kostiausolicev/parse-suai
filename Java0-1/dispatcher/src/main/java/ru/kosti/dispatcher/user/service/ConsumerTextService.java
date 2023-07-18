package ru.kosti.dispatcher.user.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.kosti.dispatcher.user.utils.message.Answer;

public interface ConsumerTextService {
    Answer commandStartHandler(Update update);
    Answer commandFindHandler(Update update);
    Answer commandMinimalPointsHandler(Update update);
    Answer commandAboutHandler(Update update);
    Answer commandHHelpHandler(Update update);
    Answer distributeOtherTextMessages(Update update);
}
