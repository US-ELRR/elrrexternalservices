/**
 *
 */
package com.deloitte.elrr.controller;

import com.deloitte.elrr.dto.ElrrStatement;
import com.deloitte.elrr.dto.XapiStatementClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.adlnet.xapi.client.StatementClient;
import gov.adlnet.xapi.model.StatementResult;
import gov.adlnet.xapi.model.Verb;
import gov.adlnet.xapi.model.Verbs;
import gov.adlnet.xapi.model.Statement;
import gov.adlnet.xapi.model.Agent;
import gov.adlnet.xapi.model.Activity;
import gov.adlnet.xapi.model.ActivityDefinition;
import gov.adlnet.xapi.model.InteractionComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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
        try {
            OffsetDateTime.parse(lastReadDate);
        } catch (DateTimeParseException e) {
            log.error("Invalid last read date");
            return ResponseEntity.badRequest().build();
        }


        StatementClient statementClient = null;
        List<ElrrStatement> listStatments = new ArrayList<>();
        try {
            statementClient = new XapiStatementClient(lrsURL,
                    lrsUsername, lrsPassword).filterBySince(lastReadDate);
            processStatemetResults(statementClient, listStatments);
        } catch (Exception e) {
            log.error("Exception:" + e.getMessage(), e);
        }
        return ResponseEntity.ok(listStatments);

    }

    /**
     *
     * @param statementClient
     * @param listStatments
     */
    private void processStatemetResults(final StatementClient statementClient,
            final List<ElrrStatement> listStatments) {
        ArrayList<Statement> adlStatementList;
        StatementResult statementResult;
        if (statementClient != null) {
            try {
                statementResult = statementClient.getStatements();
                adlStatementList = statementResult.getStatements();
            } catch (Exception e) {
                log.error("Exception:" + e.getMessage(), e);
                adlStatementList = getStatmentsList();
            }
            for (Statement statement : adlStatementList) {
                processLRSStatement(listStatments, statement);
            }
            log.info("End Of All StatementsTotal no of records are :"
                    + adlStatementList.size());
        }
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
                if (activity.getDefinition() != null && activity.
                        getDefinition().getName() != null) {
                    elrrStatement.setCourseName(activity.
                            getDefinition().getName().get("en-US"));
                }
                if (statement.getVerb() != null
                        && statement.getVerb().getId() != null) {
                    verb = getLastToken(statement.getVerb().getId(), "/");
                    log.info("verb after tokenizer: " + verb);
                }
            }
            elrrStatement.setStatementjson(new ObjectMapper().
                    writeValueAsString(statement));
            listStatments.add(elrrStatement);
        } catch (Exception e) {
            log.error("ErrorActor: ", e);
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

    /**
     *
     * @return List<ElrrStatement>
     */
    private static  ArrayList<Statement> getStatmentsList() {
        String newConstant = "http://example.com";
        ArrayList<Statement> listStatments = new ArrayList<>();
        Statement statement = new Statement();
        Agent agent = new Agent();
        Verb verb = Verbs.experienced();
        agent.setMbox("mailto:test@example.com");
        agent.setName("Tester McTesterson");
        statement.setActor(agent);
        statement.setId(UUID.randomUUID().toString());
        statement.setVerb(verb);
        Activity a = new Activity();
        a.setId(newConstant);
        statement.setObject(a);
        ActivityDefinition ad = new ActivityDefinition();
        ad.setChoices(new ArrayList<InteractionComponent>());
        InteractionComponent ic = new InteractionComponent();
        ic.setId(newConstant);
        ic.setDescription(new HashMap<>());
        ic.getDescription().put("en-US", "test");
        ad.getChoices().add(ic);
        ad.setInteractionType("choice");
        ad.setMoreInfo(newConstant);
        a.setDefinition(ad);
        listStatments.add(statement);
        return listStatments;
    }
}
