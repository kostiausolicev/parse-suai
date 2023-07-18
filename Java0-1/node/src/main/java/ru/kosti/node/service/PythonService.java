package ru.kosti.node.service;

import org.springframework.http.ResponseEntity;
import ru.kosti.ResponseType;

public interface PythonService {
    ResponseEntity<ResponseType> getApplicantInformation (String vtr, String snils, long telegramId);
    ResponseEntity<ResponseType> getVectorsList();
    ResponseEntity<ResponseType> getCsvFile(String vtr, long telegramId);
    ResponseEntity<ResponseType> getHtmlFile(String vtr, long telegramId);
}
