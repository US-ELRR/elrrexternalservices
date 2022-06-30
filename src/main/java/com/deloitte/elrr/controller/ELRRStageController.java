/**
 *
 */
package com.deloitte.elrr.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deloitte.elrr.dto.ElrrStatement;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.adlnet.xapi.client.StatementClient;
import gov.adlnet.xapi.model.Activity;
import gov.adlnet.xapi.model.Statement;
import gov.adlnet.xapi.model.StatementResult;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mnelakurti
 *
 */
@RestController
@RequestMapping("api")
@Slf4j
public class ELRRStageController {

    /**
     *
     */
    @Value("${lrs.url}")
    private String lrsURL;

    /**
     *
     */
    @Value("${lrs.username}")
    private String lrsUsername;

    /**
     *
     */
    @Value("${lrs.password}")
    private String lrsPassword;

    /**
     * This for returning the localData.
     *
     * @param lastReadDate String
     * @return ResponseEntity
     */
    @GetMapping("/lrsdata")
    public ResponseEntity<List<ElrrStatement>> localData(
            @RequestParam(value = "lastReadDate",
            defaultValue = "2021-01-02T00:00:00Z")
            final String lastReadDate) {
        StatementResult statementResult = null;
        StatementClient statementClient = null;
        List<ElrrStatement> listStatments = new ArrayList<>();
                try {
            statementClient = new StatementClient(lrsURL,
                    lrsUsername, lrsPassword);
            statementResult = statementClient.
                    filterBySince(lastReadDate).getStatements();
            for (Statement statement : statementResult.getStatements()) {
                processLRSStatement(listStatments, statement);
            }
            log.info("End Of All StatementsTotal no of records are :"
                    + statementResult.getStatements().size());

        } catch (IOException e) {
            log.error("IOException:" + e.getMessage());
        }
        return ResponseEntity.ok(listStatments);

    }

    /**
     * @param listStatments
     * @param statement
     */
    private void processLRSStatement(final List<ElrrStatement> listStatments,
            final Statement statement) {
        String verb;
        try {
            ElrrStatement elrrStatement = new ElrrStatement();
            elrrStatement.setActor(statement.getActor().getMbox());
            elrrStatement.setActorName(statement.getActor().getName());
            elrrStatement.setVerb(statement.getVerb().getId());
            if (statement.getObject() != null && "Activity".
                    equalsIgnoreCase(statement.getObject().getObjectType())) {
                Activity activity = (Activity) statement.getObject();
                elrrStatement.setActivity(activity.getId());
                if (activity.getDefinition() != null) {
                    elrrStatement.setCourseName(activity.
                            getDefinition().getName().get("en-US"));
                }
                verb = getLastToken(statement.getVerb().getId(), "/");
                log.info("verb after tokenizer: " + verb);
            }
            elrrStatement.setStatementjson(new ObjectMapper().
                    writeValueAsString(statement));
            listStatments.add(elrrStatement);
        } catch (Exception e) {
            log.error("ErrorActor: ", statement.getActor());
        }
    }

    /**
     *
     * @param strValue
     * @param splitter
     * @return String
     */
    private String getLastToken(final String strValue,
            final String splitter) {
        String[] strArray = strValue.split(splitter);
        return strArray[strArray.length - 1];
    }
}
