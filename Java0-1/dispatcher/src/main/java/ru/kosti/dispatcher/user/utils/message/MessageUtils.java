package ru.kosti.dispatcher.user.utils.message;

import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

public class MessageUtils {
    public static SendMessage createMessage(Update update, String text) {
        var message = new SendMessage();
        if (update.hasMessage()) message.setChatId(update.getMessage().getChatId());
        else if (update.hasCallbackQuery()) message.setChatId(update.getCallbackQuery().getFrom().getId());
        message.setText(text);
        return message;
    }

    public static Map<String, String> parseCallbackData(String callbackData) {
        var rawData = callbackData.split(";");
        var parseData = new HashMap<String, String>();
        parseData.put("vector", rawData[0]);
        parseData.put("action", rawData[1]);
        return parseData;
    }
}
