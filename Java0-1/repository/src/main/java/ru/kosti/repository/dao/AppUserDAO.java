package ru.kosti.repository.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.kosti.repository.entity.AppUser;
import ru.kosti.repository.entity.enums.AppUserStates;

public interface AppUserDAO extends JpaRepository<AppUser, Long> {
    long findTelegramIdByUuid(String uuid);
    @Modifying
    @Transactional
    @Query("UPDATE AppUser u SET u.states = :newState WHERE u.telegramId = :telegramId")
    void updateStatesByTelegramId(@Param("newState") AppUserStates newState, @Param("telegramId") long telegramId);

    @Modifying
    @Transactional
    @Query("UPDATE AppUser u SET u.snils = :newSnils WHERE u.telegramId = :telegramId")
    void updateSnilsByTelegramId(@Param("newSnils") String snils, @Param("telegramId") long telegramId);

    @Modifying
    @Transactional
    @Query("UPDATE AppUser u SET u.uuid = :newUuid WHERE u.telegramId = :telegramId")
    void updateUuidByTelegramId(@Param("newUuid") String uuid, @Param("telegramId") long telegramId);

    AppUser findAppUserByTelegramId(long telegramId);

    default void saveIfTelegramUserNotExist(AppUser user) {
        AppUser existingUser  = findAppUserByTelegramId(user.getTelegramId());
        if (existingUser  != null) return;
        save(user);
    }
}
