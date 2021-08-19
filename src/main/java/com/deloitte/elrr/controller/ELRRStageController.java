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
import com.fasterxml.classmate.GenericType;

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
	public ResponseEntity<List<ElrrStatement>> localData(@RequestParam(value = "lastReadDate", defaultValue = "2021-01-02T00:00:00Z") String lastReadDate) throws ResourceNotFoundException {
				StatementResult statementResult = null;
				StatementClient statementClient = null;
				String verb=null;
				List<ElrrStatement>  listStatments = new ArrayList<ElrrStatement>();
				Jsonb jsonb = JsonbBuilder.create();
			
				try {
						statementClient = new StatementClient(lrsURL, lrsUsername, lrsPassword);
						System.out.println("statementClient: "+statementClient);
						System.out.println(""+statementClient.filterBySince(lastReadDate));
						statementResult = statementClient.filterBySince(lastReadDate).getStatements();
						for (Statement statement : statementResult.getStatements()) {
								try {
										ElrrStatement elrrStatement = new ElrrStatement();
										System.out.println("Actor: "+statement.getActor());
										elrrStatement.setActor(statement.getActor().getMbox());
										elrrStatement.setActorName(statement.getActor().getName());
										System.out.println("Verb: "+statement.getVerb());
										elrrStatement.setVerb(statement.getVerb().getId());
										System.out.println("Authority: "+statement.getAuthority());
										System.out.println("Object: "+statement.getObject());
										System.out.println("Object Type: "+statement.getObject().getObjectType());
										System.out.println("Result: "+statement.getResult());
										System.out.println("Attachments: "+statement.getAttachments());
										Calendar statementTimestamp = javax.xml.bind.DatatypeConverter.parseDateTime(statement.getTimestamp());
										System.out.println("Statement Date:"+statementTimestamp);
										if(statement.getObject()!=null && "Activity".equalsIgnoreCase(statement.getObject().getObjectType())) {
											Activity activity = (Activity) statement.getObject();
											elrrStatement.setActivity(activity.getId());
											if(activity.getDefinition()!=null) {
												elrrStatement.setCourseName(activity.getDefinition().getName().get("en-US"));
											}	
											verb=getLastToken(statement.getVerb().getId(),"/");
										    System.out.println("verb after tokenizer: "+verb);
										}						
										elrrStatement.setStatementjson(jsonb.toJson(statement));
										listStatments.add(elrrStatement);
								}catch(Exception e) {
									System.out.println("ErrorActor: "+statement.getActor());
									e.printStackTrace();
								}
							}
						
						
						//String result = jsonb.toJson(statementResult.getStatements());
						//List<Statement> serializedBookList = jsonb.fromJson(result, new GenericType<List<Statement>>(){}.getClass().getGenericSuperclass());
						//System.out.println("serializedStatement:"+result);
						System.out.println("New End Of All Statements ################  Total no of records are :"+statementResult.getStatements().size());
						   
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			return  ResponseEntity.ok(listStatments);
		
	}
	
	private String getLastToken(String strValue, String splitter )  
	{        
	   String[] strArray = strValue.split(splitter);  
	   return strArray[strArray.length -1];            
	}
}
