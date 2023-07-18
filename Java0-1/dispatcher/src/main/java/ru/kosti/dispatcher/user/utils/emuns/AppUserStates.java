package ru.kosti.dispatcher.user.utils.emuns;

public enum AppUserStates {
    BASIC_STATE("BASIC_STATE"),
    WAIT_FOR_SNILS_STATE("WAIT_FOR_SNILS_STATE"),
    SEND_RESULTS_STATE("SEND_RESULTS_STATE");

    private final String stringPresentation;

    AppUserStates(String stringPresentation) {
        this.stringPresentation = stringPresentation;
    }

    @Override
    public String toString() {
        return this.stringPresentation;
    }
}
