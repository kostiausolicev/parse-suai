package ru.kosti.dispatcher.user.service;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.kosti.dispatcher.user.utils.message.Answer;

public interface ConsumerCallbackService {
    Answer distributeCallbackData(Update update);
}
