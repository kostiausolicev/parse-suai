package ru.kosti.node.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.kosti.ResponseType;
import ru.kosti.node.proxy.ApplicantProxy;
import ru.kosti.node.service.PythonService;
import ru.kosti.repository.dao.AppUserDAO;

import java.util.Objects;
import java.util.UUID;

@Service
public class PythonServiceImpl implements PythonService {
    private final ApplicantProxy proxy;
    private final AppUserDAO appUserDAO;

    public PythonServiceImpl(ApplicantProxy proxy, AppUserDAO appUserDAO) {
        this.proxy = proxy;
        this.appUserDAO = appUserDAO;
    }

    @Override
    // TODO Сделать аннотации для проверки uuid кодов, для красоты
    // TODO Сделать более подробную проверку входящих соединений
    public ResponseEntity<ResponseType> getApplicantInformation(String vtr, String snils, String type_of_list, long telegramId) {
        var uuid = UUID.randomUUID().toString();
        appUserDAO.updateUuidByTelegramId(uuid, telegramId);
        var response = proxy.getApplicantInformation(vtr, snils, type_of_list, uuid, telegramId);
        if (response.getStatusCode() != HttpStatus.OK || !response.hasBody()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (!appUserDAO.findAppUserByTelegramId(telegramId).getUuid().equals(response.getBody().getUuid())) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return response;
    }

    @Override
    public ResponseEntity<ResponseType> getVectorsList() {
        var uuid = UUID.randomUUID().toString();
        var response = proxy.getVectorsList(uuid);
        if (uuid.equals(Objects.requireNonNull(response.getBody()).getUuid())) {
            return response;
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ResponseType> getVectorsList1(String vtr) {
        var uuid = UUID.randomUUID().toString();
        var response = proxy.getVectorList1(uuid, vtr);
        if (uuid.equals(Objects.requireNonNull(response.getBody()).getUuid())) {
            return response;
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ResponseType> getCsvFile(String vtr, long telegramId, String type_of_list) {
        var uuid = UUID.randomUUID().toString();
        var response = proxy.getCsvFile(vtr, uuid, telegramId, type_of_list);
        if (uuid.equals(Objects.requireNonNull(response.getBody()).getUuid())) {
            return response;
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ResponseType> getHtmlFile(String vtr, long telegramId, String type_of_list) {
        var uuid = UUID.randomUUID().toString();
        var response = proxy.getHtmlFile(vtr, uuid, telegramId, type_of_list);
        if (uuid.equals(Objects.requireNonNull(response.getBody()).getUuid())) {
            return response;
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ResponseType> getVectorData(String vtr, long telegramId) {
        var uuid = UUID.randomUUID().toString();
        var response = proxy.getVectorData(uuid, vtr, telegramId);
        if (uuid.equals(Objects.requireNonNull(response.getBody()).getUuid())) {
            return response;
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
