package ru.kosti.dispatcher.user.utils.keyboards;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import ru.kosti.dispatcher.user.proxy.NodeProxy;
import ru.kosti.dispatcher.user.utils.emuns.CallbackActions;

import java.util.ArrayList;
import java.util.List;

@Component
public class InlineKeyboards {
    private final NodeProxy proxy;

    public InlineKeyboards(NodeProxy proxy) {
        this.proxy = proxy;
    }

    public InlineKeyboardMarkup getAboutKeyboard() {
        var keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        var row1 = new ArrayList<InlineKeyboardButton>();
        var row2 = new ArrayList<InlineKeyboardButton>();
        var button1 = new InlineKeyboardButton();
        var button2 = new InlineKeyboardButton();
        var button3 = new InlineKeyboardButton();

        button1.setText("Написать usoltsev_k");
        button1.setUrl("https://t.me/usoltsev_k");
        button2.setText("Написать danil");
        button2.setUrl("https://t.me/urodina");
        button3.setText("github");
        button3.setUrl("https://github.com/");

        row1.add(button1);
        row1.add(button2);
        row2.add(button3);
        rows.add(row1);
        rows.add(row2);
        keyboard.setKeyboard(rows);
        return keyboard;
    }

    public InlineKeyboardMarkup getChooseVectorKeyboard(CallbackActions action) {
        var response = proxy.getAllVectorsInformation();
        if (response.getStatusCode() != HttpStatus.OK || (response.getBody() == null || !response.getBody().hasVectorList()))
            return null;
        var vectors = response.getBody().getVectorList();
        var keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttonRows = new ArrayList<>();
        for (int i = 0; i < vectors.size(); i++) {
            var button = new InlineKeyboardButton();
            var vector = vectors.get(i).substring(0, 15).replace("_", ".");
            button.setText(vector);
            button.setCallbackData(vector.substring(0, 8).replace(".", "_") + ";" + action.toString());
            buttonRows.add(new ArrayList<>());
            buttonRows.get(i).add(button);
        }
        keyboard.setKeyboard(buttonRows);
        return keyboard;
    }

    // TODO Убрать дубликаты
    public InlineKeyboardMarkup getChooseListForParseKeyboard(String vector) {
        var response = proxy.getAllVectorsInformation2(vector);
        if (response.getStatusCode() != HttpStatus.OK || (response.getBody() == null || !response.getBody().hasVectorList()))
            return null;
        var lists = response.getBody().getVectorList();
        var keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttonRows = new ArrayList<>();
        for (int i = 0; i < lists.size(); i++) {
            var button = new InlineKeyboardButton();
            var list = lists.get(i);
            button.setText(list);
            button.setCallbackData(vector + ";" + CallbackActions.FIND + ";" + list);
            buttonRows.add(new ArrayList<>());
            buttonRows.get(i).add(button);
        }
        keyboard.setKeyboard(buttonRows);
        return keyboard;
    }

    public InlineKeyboardMarkup webAppKeyboard(String vector) {
        var webApp = new WebAppInfo();
        webApp.setUrl("https://kostiausolicev.github.io/");
        var keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttonRows = new ArrayList<>();
        var row1 = new ArrayList<InlineKeyboardButton>();
        var row2 = new ArrayList<InlineKeyboardButton>();
        var row3 = new ArrayList<InlineKeyboardButton>();
        var button1 = new InlineKeyboardButton();
        button1.setText("Открыть в телеграм");
        button1.setWebApp(webApp);
        row1.add(button1);
        var button2 = new InlineKeyboardButton();
        button2.setText("Скачать csv");
        button2.setCallbackData(vector + ";" + CallbackActions.WAIT_SCV);
        var button3 = new InlineKeyboardButton();
        button3.setText("Скачать html");
        button3.setCallbackData(vector + ";" + CallbackActions.WAIT_HTML);
        var button4 = new InlineKeyboardButton();
        button4.setText("Закончить обзор");
        button4.setCallbackData(vector + ";" + CallbackActions.CANSEL);
        row3.add(button4);
        row2.add(button2);
        row2.add(button3);
        buttonRows.add(row1);
        buttonRows.add(row2);
        buttonRows.add(row3);
        keyboard.setKeyboard(buttonRows);
        return keyboard;
    }
}
