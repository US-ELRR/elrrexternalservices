/**
 * 
 */
package com.deloitte.elrr.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deloitte.elrr.dto.ElrrStatement;
import com.deloitte.elrr.exception.ResourceNotFoundException;

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

	@Value("${lrs.url}")
	private String lrsURL;
	@Value("${lrs.username}")
	private String lrsUsername;
	@Value("${lrs.password}")
	private String lrsPassword;

	@GetMapping("/lrsdata")
	public ResponseEntity<List<ElrrStatement>> localData(
			@RequestParam(value = "lastReadDate", defaultValue = "2021-01-02T00:00:00Z") String lastReadDate) {
		StatementResult statementResult = null;
		StatementClient statementClient = null;
		List<ElrrStatement> listStatments = new ArrayList<>();
		Jsonb jsonb = JsonbBuilder.create();

		try {
			statementClient = new StatementClient(lrsURL, lrsUsername, lrsPassword);
			log.info("statementClient: " + statementClient);
			log.info("" + statementClient.filterBySince(lastReadDate));
			statementResult = statementClient.filterBySince(lastReadDate).getStatements();
			for (Statement statement : statementResult.getStatements()) {
				processLRSStatement(listStatments, jsonb, statement);
			}
			log.info("New End Of All Statements ################  Total no of records are :"
					+ statementResult.getStatements().size());

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				jsonb.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return ResponseEntity.ok(listStatments);

	}

	/**
	 * @param listStatments
	 * @param jsonb
	 * @param statement
	 */
	private void processLRSStatement(List<ElrrStatement> listStatments, Jsonb jsonb, Statement statement) {
		String verb;
		try {
			ElrrStatement elrrStatement = new ElrrStatement();
			log.info("Actor: " + statement.getActor());
			elrrStatement.setActor(statement.getActor().getMbox());
			elrrStatement.setActorName(statement.getActor().getName());
			log.info("Verb: " + statement.getVerb());
			elrrStatement.setVerb(statement.getVerb().getId());
			log.info("Authority: " + statement.getAuthority());
			log.info("Object: " + statement.getObject());
			log.info("Object Type: " + statement.getObject().getObjectType());
			log.info("Result: " + statement.getResult());
			log.info("Attachments: " + statement.getAttachments());
			Calendar statementTimestamp = javax.xml.bind.DatatypeConverter.parseDateTime(statement.getTimestamp());
			log.info("Statement Date:" + statementTimestamp);
			if (statement.getObject() != null && "Activity".equalsIgnoreCase(statement.getObject().getObjectType())) {
				Activity activity = (Activity) statement.getObject();
				elrrStatement.setActivity(activity.getId());
				if (activity.getDefinition() != null) {
					elrrStatement.setCourseName(activity.getDefinition().getName().get("en-US"));
				}
				verb = getLastToken(statement.getVerb().getId(), "/");
				log.info("verb after tokenizer: " + verb);
			}
			elrrStatement.setStatementjson(jsonb.toJson(statement));
			listStatments.add(elrrStatement);
		} catch (Exception e) {
			log.info("ErrorActor: " + statement.getActor());
			e.printStackTrace();
		}
	}

	private String getLastToken(String strValue, String splitter) {
		String[] strArray = strValue.split(splitter);
		return strArray[strArray.length - 1];
	}
}
