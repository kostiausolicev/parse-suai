package ru.kosti.node.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kosti.node.controller.enums.UserColumn;
import ru.kosti.node.service.ApplicantService;

@RestController
public class NodeServiceController {
    private final ApplicantService applicantService;

    public NodeServiceController(ApplicantService applicantService) {
        this.applicantService = applicantService;
    }

    @PostMapping("/check_user/{telegramId}")
    public ResponseEntity<Void> createUser(
            @PathVariable long telegramId
    ) {
        applicantService.createUser(telegramId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping ("/update/{telegramId}")
    public ResponseEntity<Void> createUser(
            @PathVariable long telegramId,
            @RequestParam UserColumn userColumn,
            @RequestParam String newValue
    ) {
        switch (userColumn) {
            case STATES -> applicantService.updateUserState(telegramId, newValue);
            case UUID -> applicantService.updateUserUUIDRequest(telegramId, newValue);
            case SNILS -> applicantService.updateUserSnils(telegramId, newValue);
            default -> {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/get_user_state/{telegramId}")
    public ResponseEntity<String> getUserState(
            @PathVariable long telegramId
    ) {
        var state = applicantService.getUserState(telegramId);
        return new ResponseEntity<>(state, HttpStatus.OK);
    }

    @GetMapping("get_user_column/{telegramId}")
    public ResponseEntity<String> getUserColumn(
            @PathVariable long telegramId,
            @RequestParam UserColumn getColumn
    ) {
        String result;
        switch (getColumn) {
            case STATES -> result = applicantService.getUserState(telegramId);
            case UUID -> result = applicantService.getUserUuid(telegramId);
            case SNILS -> result = applicantService.getUserSnils(telegramId);
            default -> {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
