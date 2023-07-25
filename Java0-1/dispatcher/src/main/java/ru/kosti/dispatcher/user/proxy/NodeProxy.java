package ru.kosti.dispatcher.user.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kosti.ResponseType;
import ru.kosti.dispatcher.user.utils.emuns.UserColumn;

@FeignClient(name = "node", url = "${node.server.url}")
public interface NodeProxy {
    @GetMapping(value = "/find/{telegramId}")
    ResponseEntity<ResponseType> getApplicantInformation(
            @RequestParam("vtr") String vtr,
            @RequestParam("snils") String snils,
            @RequestParam("type_of_list") String type_of_list,
            @PathVariable long telegramId);

    @GetMapping("/find_vector_inform/{telegramId}")
    ResponseEntity<ResponseType> getVectorData(
            @RequestParam("vtr") String vtr,
            @PathVariable long telegramId);

    @GetMapping(value = "/all_vectors")
    ResponseEntity<ResponseType> getAllVectorsInformation();

    @GetMapping(value = "/all_vectors_lists")
    ResponseEntity<ResponseType> getAllVectorsInformation2(
            @RequestParam("vtr") String vector
    );

    @PostMapping(value = "/check_user/{telegramId}")
    ResponseEntity<Void> createUser(
        @PathVariable long telegramId
    );

    @GetMapping(value = "/get_user_column/{telegramId}")
    ResponseEntity<String> getUserState(
            @PathVariable long telegramId
    );

    @GetMapping("get_user_column/{telegramId}")
    ResponseEntity<String> getUserColumn(
            @PathVariable long telegramId,
            @RequestParam UserColumn getColumn
    );

    @PutMapping(value = "/update/{telegramId}")
    ResponseEntity<Void> updateInformation(
            @PathVariable long telegramId,
            @RequestParam UserColumn userColumn,
            @RequestParam String newValue
    );

    @GetMapping(value = "/csv/{telegramId}")
    ResponseEntity<ResponseType> getCsvFile(
            @RequestParam("vtr") String vtr,
            @RequestParam("type_of_list") String type_of_list,
            @PathVariable long telegramId);

    @GetMapping(value = "/html/{telegramId}")
    ResponseEntity<ResponseType> getHtmlFile(
            @RequestParam("vtr") String vtr,
            @RequestParam("type_of_list") String type_of_list,
            @PathVariable long telegramId);
}
