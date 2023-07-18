package ru.kosti.dispatcher.user.utils.keyboards;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.kosti.dispatcher.user.proxy.NodeProxy;
import ru.kosti.dispatcher.user.utils.emuns.UserColumn;

import java.util.ArrayList;

import static ru.kosti.dispatcher.user.utils.message.MessageUtils.*;

@Component
public class ChatKeyboards {
    private final NodeProxy proxy;

    public ChatKeyboards(NodeProxy proxy) {
        this.proxy = proxy;
    }

    public ReplyKeyboardMarkup getBaseChatKeyboard() {
        final ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        var rows = new ArrayList<KeyboardRow>();
        var row1 = new KeyboardRow();
        var row2 = new KeyboardRow();
        var row3 = new KeyboardRow();
        row1.add("Поиск");
        row2.add("Помощь");
        row2.add("О боте");
        row3.add("Мин баллы прошлых лет");
        rows.add(row1);
        rows.add(row3);
        rows.add(row2);
        keyboard.setKeyboard(rows);
        keyboard.setResizeKeyboard(true);
        keyboard.setOneTimeKeyboard(false);
        return keyboard;
    }

    public SendMessage getWaitForSnilsKeyboard(Update update) {
        var message = createMessage(update, "Введите снилс или выберите соответсвующее действие на клавиатуре");
        message.setReplyMarkup(getWaitForSnilsMarkup(update));
        return message;
    }

    public ReplyKeyboardMarkup getWaitForSnilsMarkup(Update update) {
        long telegramId = update.getMessage().getChatId();
        var snils = proxy.getUserColumn(telegramId, UserColumn.SNILS).getBody();

        final ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        var rows = new ArrayList<KeyboardRow>();
        var row1 = new KeyboardRow();
        row1.add("Отмена");
        row1.add("Я посмотреть");
        rows.add(row1);
        if (snils != null) {
            var row2 = new KeyboardRow();
            row2.add(snils);
            rows.add(row2);
        }
        keyboard.setKeyboard(rows);
        keyboard.setResizeKeyboard(true);
        keyboard.setOneTimeKeyboard(true);
        return keyboard;
    }
}
