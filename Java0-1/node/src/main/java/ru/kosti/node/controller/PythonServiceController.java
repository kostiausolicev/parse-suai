package ru.kosti.node.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kosti.ResponseType;
import ru.kosti.node.service.PythonService;

@RestController
public class PythonServiceController {
    private final PythonService pythonService;

    public PythonServiceController(PythonService applicantService) {
        this.pythonService = applicantService;
    }

    @GetMapping("/find/{telegramId}")
    public ResponseEntity<ResponseType> getApplicantInformation(
            @RequestParam("vtr") String vtr,
            @RequestParam("snils") String snils,
            @PathVariable long telegramId
    ) {
        return pythonService.getApplicantInformation(vtr, snils, telegramId);
    }

    @GetMapping("/all_vectors")
    public ResponseEntity<ResponseType> getAllVectorsInformation() {
        return pythonService.getVectorsList();
    }

    @GetMapping(value = "/csv/{telegramId}")
    public ResponseEntity<ResponseType> getCsvFile(
            @RequestParam("vtr") String vtr,
            @PathVariable long telegramId) {
        return pythonService.getCsvFile(vtr, telegramId);
    }

    @GetMapping(value = "/html/{telegramId}")
    ResponseEntity<ResponseType> getHtmlFile(
            @RequestParam("vtr") String vtr,
            @PathVariable long telegramId) {
        return pythonService.getHtmlFile(vtr, telegramId);
    }
}
