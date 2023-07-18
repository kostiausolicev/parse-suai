package ru.kosti.node.service.impl;

import org.springframework.stereotype.Service;
import ru.kosti.node.service.ApplicantService;
import ru.kosti.repository.dao.AppUserDAO;
import ru.kosti.repository.entity.AppUser;
import ru.kosti.repository.entity.enums.AppUserStates;

@Service
public class ApplicantServiceImpl implements ApplicantService {
    private final AppUserDAO appUserDAO;

    public ApplicantServiceImpl(AppUserDAO appUserDAO) {
        this.appUserDAO = appUserDAO;
    }

    @Override
    public void createUser(long telegramId) {
        var user = new AppUser();
        user.setTelegramId(telegramId);
        user.setStates(AppUserStates.BASIC_STATE);
        user.setSnils("");
        user.setUuid("");
        appUserDAO.saveIfTelegramUserNotExist(user);
    }

    @Override
    public void updateUserState(long telegramId, String userState) {
        AppUserStates state = AppUserStates.valueOf(userState);
        appUserDAO.updateStatesByTelegramId(state, telegramId);
    }

    @Override
    public void updateUserSnils(long telegramId, String snils) {
        appUserDAO.updateSnilsByTelegramId(snils, telegramId);
    }

    @Override
    public void updateUserUUIDRequest(long telegramId, String uuid) {
        appUserDAO.updateUuidByTelegramId(uuid, telegramId);
    }

    @Override
    public String getUserState(long telegramId) {
        return appUserDAO.findAppUserByTelegramId(telegramId).getStates().toString();
    }

    @Override
    public String getUserSnils(long telegramId) {
        return appUserDAO.findAppUserByTelegramId(telegramId).getSnils();
    }

    @Override
    public String getUserUuid(long telegramId) {
        return appUserDAO.findAppUserByTelegramId(telegramId).getUuid();
    }
}
