package com.deloitte.elrr.controller;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yetanalytics.xapi.client.LRS;
import com.yetanalytics.xapi.client.StatementClient;
import com.yetanalytics.xapi.client.filters.StatementFilters;
import com.yetanalytics.xapi.model.Statement;

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
     * @param lastReadDate
     * @return RersponseEntity
     */
    @SuppressWarnings("checkstyle:linelength")
    @GetMapping("/lrsdata")
    public ResponseEntity<List<Statement>> localData(
            @RequestParam(value = "lastReadDate", defaultValue = "2021-01-02T00:00:00Z") final String lastReadDate) {

        List<Statement> result = null;

        try {

            OffsetDateTime.parse(lastReadDate);

            LRS lrs = new LRS(lrsURL, lrsUsername, lrsPassword);
            StatementFilters filters = new StatementFilters();
            filters.setSince(lastReadDate);

            StatementClient client = new StatementClient(lrs);
            result = client.getStatements(filters);

        } catch (DateTimeParseException e) {
            log.error("Invalid last read date", e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Exception", e);
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(result);

    }
}
