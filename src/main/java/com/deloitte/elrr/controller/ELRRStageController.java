package com.deloitte.elrr.controller;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
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
import com.yetanalytics.xapi.exception.StatementClientException;
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
     * @param maxStatements
     * @return RersponseEntity
     */
    @SuppressWarnings("checkstyle:linelength")
    @GetMapping("/lrsdata")
    public ResponseEntity<List<Statement>> localData(@RequestParam(
            value = "lastReadDate") final ZonedDateTime lastReadDate,
            @RequestParam(value = "maxStatements") final int maxStatements) {
        List<Statement> result = null;

        try {

            OffsetDateTime.parse(lastReadDate.toString());

            LRS lrs = new LRS(lrsURL, lrsUsername, lrsPassword);
            StatementFilters filters = new StatementFilters();
            filters.setSince(lastReadDate);
            filters.setAscending(true);

            StatementClient client = new StatementClient(lrs);
            result = client.getStatements(filters, maxStatements);

        } catch (DateTimeParseException e) {
            log.error("Invalid last read date", e);
            return ResponseEntity.badRequest().build();
        } catch (StatementClientException e) {
            log.error("Error getting statements", e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Exception", e);
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(result);

    }
}
