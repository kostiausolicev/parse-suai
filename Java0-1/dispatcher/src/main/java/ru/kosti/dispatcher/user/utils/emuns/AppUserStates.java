package ru.kosti.dispatcher.user.utils.emuns;

public enum AppUserStates {
    BASIC_STATE("BASIC_STATE"), // Следущее сообщение пользователя - команда боту
    WAIT_FOR_SNILS_STATE("WAIT_FOR_SNILS_STATE"), // Следующее сообщение пользователя - снилс
    WAIT_FOR_VECTOR_STATE("WAIT_FOR_VECTOR_STATE"), // Следующий update от пользователя - направление
    WAIT_FOR_LIST_STATE("WAIT_FOR_LIST_STATE"), // Следующий update от пользователя - тип конкурсного списка
    FIND_STATE("FIND_STATE"), // Пользователь ждет результат поиска
    SEND_RESULTS_STATE("SEND_RESULTS_STATE"); // Следующий update от пользователя - как он откроет результат

    private final String stringPresentation;

    AppUserStates(String stringPresentation) {
        this.stringPresentation = stringPresentation;
    }

    @Override
    public String toString() {
        return this.stringPresentation;
    }
}
