package ru.kosti.dispatcher.user.utils.emuns;

public enum CallbackActions {
    FIND("FIND"), // Ищем позицию человека
    CHOOSE_LIST("CHOOSE_LIST"), // Выбираем список для парсинга
    MINIMAL_POINTS("MINIMAL_POINTS"), // Ищем минимальные проходные баллы
    WAIT_HTML("WAIT_HTML"), // Отправляем html файл
    WAIT_SCV("WAIT_SCV"), // Отправляем csv файл
    CANSEL("CANSEL"); // Отменяем действие

    private final String stringPresentation;

    CallbackActions(String stringPresentation) {
        this.stringPresentation = stringPresentation;
    }

    @Override
    public String toString() {
        return this.stringPresentation;
    }
}
