package ru.kosti.dispatcher.user.utils.message;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Getter
@Setter
@NoArgsConstructor
public class Answer {
    private SendMessage sendMessage;
    private SendDocument sendDocument;

    public boolean hasMessage() {
        return sendMessage != null;
    }

    public boolean hasDoc() {
        return sendDocument != null;
    }
}
