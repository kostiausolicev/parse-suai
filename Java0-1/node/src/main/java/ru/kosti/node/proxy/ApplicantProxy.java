package ru.kosti.node.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kosti.ResponseType;

@FeignClient(name = "applicant", url = "${python.server.applicant.url}")
public interface ApplicantProxy {
    @GetMapping(value = "/find/{telegramId}")
    ResponseEntity<ResponseType> getApplicantInformation(
            @RequestParam("vtr") String vector,
            @RequestParam("snils") String snils,
            @RequestParam("type_of_list") String type_of_list,
            @RequestHeader String uuid,
            @PathVariable long telegramId);

    @GetMapping("/find_vector_inform/{telegramId}")
    ResponseEntity<ResponseType> getVectorData(
            @RequestHeader String uuid,
            @RequestParam("vtr") String vtr,
            @PathVariable long telegramId);

    @GetMapping(value = "/all_vectors")
    ResponseEntity<ResponseType> getVectorsList(@RequestHeader String uuid);

    @GetMapping(value = "/all_vectors_lists")
    ResponseEntity<ResponseType> getVectorList1(
            @RequestHeader String uuid,
            @RequestParam String vtr
    );

    @GetMapping(value = "/csv/{telegramId}")
    ResponseEntity<ResponseType> getCsvFile(
            @RequestParam("vtr") String vector,
            @RequestHeader String uuid,
            @PathVariable long telegramId);

    @GetMapping(value = "/html/{telegramId}")
    ResponseEntity<ResponseType> getHtmlFile(@RequestParam("vtr") String vector,
                                             @RequestHeader String uuid,
                                             @PathVariable long telegramId);
}
