package ru.kosti.dispatcher.user.utils.emuns;

public enum UserColumn {
    UUID("uuid"),
    SNILS("snils"),
    STATES("states");

    private final String stringPresentation;

    UserColumn(String stringPresentation) {
        this.stringPresentation = stringPresentation;
    }

    @Override
    public String toString() {
        return this.stringPresentation;
    }
}
