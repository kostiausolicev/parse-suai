package ru.kosti.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.kosti.repository.entity.enums.AppUserStates;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "app_user")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long telegramId;
    @Enumerated(EnumType.STRING)
    private AppUserStates states;
    private String snils;
    private String uuid;
}
