package ru.kosti.dispatcher.user.utils.emuns;

public enum CallbackActions {
    FIND("FIND"),
    MINIMAL_POINTS("MINIMAL_POINTS"),
    WAIT_HTML("WAIT_HTML"),
    WAIT_SCV("WAIT_SCV"),
    WAIT_WEBAPP("WAIT_WEBAPP"),
    CANSEL("CANSEL");

    private final String stringPresentation;

    CallbackActions(String stringPresentation) {
        this.stringPresentation = stringPresentation;
    }

    @Override
    public String toString() {
        return this.stringPresentation;
    }
}
