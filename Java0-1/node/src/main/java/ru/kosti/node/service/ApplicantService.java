package ru.kosti.node.service;

public interface ApplicantService {
    void createUser(long telegramId);
    void updateUserState(long telegramId, String userState);
    void updateUserSnils(long telegramId, String snils);
    void updateUserUUIDRequest(long telegramId, String uuid);
    String getUserState(long telegramId);
    String getUserUuid(long telegramId);
    String getUserSnils(long telegramId);
}
