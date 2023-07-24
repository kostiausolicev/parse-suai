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
            @RequestParam("type_of_list") String type_of_list,
            @PathVariable long telegramId
    ) {
        return pythonService.getApplicantInformation(vtr, snils, type_of_list, telegramId);
    }

    @GetMapping("/find_vector_inform/{telegramId}")
    ResponseEntity<ResponseType> getVectorData(
            @RequestParam("vtr") String vtr,
            @PathVariable long telegramId) {
        return pythonService.getVectorData(vtr, telegramId);
    }

    @GetMapping("/all_vectors")
    public ResponseEntity<ResponseType> getAllVectorsInformation() {
        return pythonService.getVectorsList();
    }

    @GetMapping("all_vectors_lists")
    public ResponseEntity<ResponseType> getAllVectorsInformation2(@RequestParam String vtr) {
        return pythonService.getVectorsList1(vtr);
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
