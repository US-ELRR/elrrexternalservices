/** */
package com.deloitte.elrr.controller;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import gov.adlnet.xapi.client.StatementClient;
import gov.adlnet.xapi.model.Actor;
import gov.adlnet.xapi.model.IStatementObject;
import gov.adlnet.xapi.model.Statement;
import gov.adlnet.xapi.model.adapters.ActorAdapter;
import gov.adlnet.xapi.model.adapters.StatementObjectAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mnelakurti
 */
@RestController
@RequestMapping("api")
@Slf4j
public class ELRRStageController {

    @Value("${lrs.url}")
    private String lrsURL;

    @Value("${lrs.username}")
    private String lrsUsername;

    @Value("${lrs.password}")
    private String lrsPassword;

    /**
     * This for returning the localData.
     *
     * @param lastReadDate String
     * @return ResponseEntity
     */
    @SuppressWarnings("checkstyle:linelength")
    @GetMapping("/lrsdata")
    public ResponseEntity<String> localData(
            @RequestParam(value = "lastReadDate", defaultValue = "2021-01-02T00:00:00Z") final String lastReadDate) {

        String result = null;

        try {
            OffsetDateTime.parse(lastReadDate);
        } catch (DateTimeParseException e) {
            log.error("Invalid last read date");
            return ResponseEntity.badRequest().build();
        }

        StatementClient statementClient = null;
        List<Statement> listStatements = new ArrayList<>();

        try {
            statementClient = new StatementClient(lrsURL, lrsUsername,
                    lrsPassword).filterBySince(lastReadDate);
            listStatements = statementClient.getStatements().getStatements();
            result = gsonToJson(listStatements);
        } catch (Exception e) {
            log.error("Exception:" + e.getMessage(), e);
        }
        return ResponseEntity.ok(result);

    }

    /**
     * @param listStatements
     * @return String
     */
    private String gsonToJson(List<Statement> listStatements) {

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Actor.class, new ActorAdapter());
        builder.registerTypeAdapter(IStatementObject.class,
                new StatementObjectAdapter());
        Gson gson = builder.create();
        return gson.toJson(listStatements);

    }
}
