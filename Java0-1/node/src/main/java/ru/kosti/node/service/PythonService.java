package ru.kosti.node.service;

import org.springframework.http.ResponseEntity;
import ru.kosti.ResponseType;

public interface PythonService {
    ResponseEntity<ResponseType> getApplicantInformation (String vtr, String snils, String type_of_list, long telegramId);
    ResponseEntity<ResponseType> getVectorsList();
    ResponseEntity<ResponseType> getVectorsList1(String vtr);
    ResponseEntity<ResponseType> getCsvFile(String vtr, long telegramId, String type_of_list);
    ResponseEntity<ResponseType> getHtmlFile(String vtr, long telegramId, String type_of_list);
    ResponseEntity<ResponseType> getVectorData(String vtr, long telegramId);
}
