/**
 *
 */
package com.deloitte.elrr.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.deloitte.elrr.dto.ElrrStatement;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

/**
 * @author mnelakurti
 *
 */
@EnableWebMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {
        com.deloitte.elrr.controller.ELRRStageController.class })
@EnableEncryptableProperties
class ELRRStageControllerTest {

    /**
    *
    */
    @Autowired
    private WebApplicationContext webApplicationContext;
    /**
    *
    */
    private MockMvc mockMvc;

    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(
                webApplicationContext).build();
    }

    @Test
    void testlocalData() throws Exception {
        List<ElrrStatement> listStatments = new ArrayList<ElrrStatement>();
        ElrrStatement elrrStatement = new ElrrStatement();
        elrrStatement.setActor("mailto:c.cooper@yahoo.com");
        elrrStatement.setActorName("Cristopher Cunningham");
        elrrStatement.setVerb("http://adlnet.gov/expapi/verbs/completed");
        elrrStatement.setActivity(
                "http://adlnet.gov/expapi/activities/example/CLC%20104");
        elrrStatement.setCourseName("Analyzing Profit or Fee");
        elrrStatement.setStatementjson("{\"actor\":{\"mbox\""
                + ":\"mailto:c.cooper@yahoo.com\","
                + "\"name\":\"Cristopher Cunningham\",\"objectType\""
                + ":\"Agent\"},\"authority\":{\"account\":{\"homePage\""
                + ":\"https://deloitte-prototype-noisy.lrs.io/keys/"
                + "ELRR_Validation\","
                + "\"name\":\"ELRR_Validation\"},\"objectType\":"
                + "\"Agent\"},\"id\""
                + ":\"4859303f-8237-46d2-8bf3-186069514eb7\","
                + "\"object\":{\"definition\""
                + ":{\"name\":{\"en-US\""
                + ":\"Analyzing Profit or Fee\"}},\"id\""
                + ":\"http://adlnet.gov/expapi/activities/example/CLC%20104\","
                + "\"objectType\":\"Activity\"},"
                + "\"result\":{\"completion\":false,"
                + "\"success\":true},\"stored\""
                + ":\"2021-05-04T23:30:40.014Z\","
                + "\"timestamp\":\"2021-05-04T23:30:40.014Z\","
                + "\"verb\":{\"display\"" + ":{\"en-US\":\"completed\"},"
                + "\"id\":\"http://adlnet.gov/expapi/verbs/completed\"}}");
        listStatments.add(elrrStatement);

        MockHttpServletRequestBuilder rquestBuilder = MockMvcRequestBuilders
                .get("/api/lrsdata?lastReadDate=2021-01-10T00:00:00Z")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = this.mockMvc.perform(rquestBuilder).andReturn();
        MockHttpServletResponse servletResponse = mvcResult.getResponse();
        HttpStatus status = HttpStatus.valueOf(servletResponse.getStatus());
        assertNotNull(status);
        assertNotNull(listStatments);
        ElrrStatement finalelrrStatement = listStatments.get(0);
        assertEquals("mailto:c.cooper@yahoo.com",
                finalelrrStatement.getActor());
    }

    @Test
    void testlocalDataSize() throws Exception {
        List<ElrrStatement> listStatments = new ArrayList<ElrrStatement>();
        ElrrStatement elrrStatement = new ElrrStatement();
        elrrStatement.setActor("mailto:c.cooper@yahoo.com");
        elrrStatement.setActorName("Cristopher Cunningham");
        elrrStatement.setVerb("http://adlnet.gov/expapi/verbs/completed");
        elrrStatement.setActivity(
                "http://adlnet.gov/expapi/activities/example/CLC%20104");
        elrrStatement.setCourseName("Analyzing Profit or Fee");
        elrrStatement.setStatementjson(
                "{\"actor\":{\"mbox\":\"mailto:c.cooper@yahoo.com\","
                + "\"name\":\"Cristopher Cunningham\",\"objectType\""
                + ":\"Agent\"},\"authority\":{\"account\":{\"homePage\""
                + ":\"https://deloitte-prototype-noisy.lrs.io/keys/"
                + "ELRR_Validation\","
                + "\"name\":\"ELRR_Validation\"},\"objectType\":"
                + "\"Agent\"},\"id\""
                + ":\"4859303f-8237-46d2-8bf3-186069514eb7\","
                + "\"object\":{\"definition\"" + ":{\"name\":{\"en-US\""
                + ":\"Analyzing Profit or Fee\"}},\"id\""
                + ":\"http://adlnet.gov/expapi/activities/example/CLC%20104\","
                + "\"objectType\":\"Activity\"},"
                + "\"result\":{\"completion\":false,"
                + "\"success\":true},\"stored\""
                + ":\"2021-05-04T23:30:40.014Z\","
                + "\"timestamp\":\"2021-05-04T23:30:40.014Z\","
                + "\"verb\":{\"display\"" + ":{\"en-US\":\"completed\"},"
                + "\"id\":\"http://adlnet.gov/expapi/verbs/completed\"}}");
        listStatments.add(elrrStatement);

        MockHttpServletRequestBuilder rquestBuilder = MockMvcRequestBuilders
                .get("/api/lrsdata?lastReadDate=2021-01-10T00:00:00Z")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = this.mockMvc.perform(rquestBuilder).andReturn();
        MockHttpServletResponse servletResponse = mvcResult.getResponse();
        assertEquals(null, servletResponse.getErrorMessage());
        ElrrStatement finalelrrStatement = listStatments.get(0);
        assertEquals(listStatments.size(), 1);
    }

    @Test
    void testlocalDataStatusOK() throws Exception {
        List<ElrrStatement> listStatments = new ArrayList<ElrrStatement>();
        ElrrStatement elrrStatement = new ElrrStatement();
        elrrStatement.setActor("mailto:c.cooper@yahoo.com");
        elrrStatement.setActorName("Cristopher Cunningham");
        elrrStatement.setVerb("http://adlnet.gov/expapi/verbs/completed");
        elrrStatement.setActivity("http://adlnet.gov/expapi"
                + "/activities/example/CLC%20104");
        elrrStatement.setCourseName("Analyzing Profit or Fee");
        elrrStatement.setStatementjson("{\"actor\":{\"mbox\""
                + ":\"mailto:c.cooper@yahoo.com\","
                + "\"name\":\"Cristopher Cunningham\",\"objectType\""
                + ":\"Agent\"},\"authority\":{\"account\":{\"homePage\""
                + ":\"https://deloitte-prototype-noisy.lrs.io/keys/"
                + "ELRR_Validation\","
                + "\"name\":\"ELRR_Validation\"},\"objectType\":"
                + "\"Agent\"},\"id\""
                + ":\"4859303f-8237-46d2-8bf3-186069514eb7\","
                + "\"object\":{\"definition\""
                + ":{\"name\":{\"en-US\""
                + ":\"Analyzing Profit or Fee\"}},\"id\""
                + ":\"http://adlnet.gov/expapi/activities/example/CLC%20104\","
                + "\"objectType\":\"Activity\"},"
                + "\"result\":{\"completion\":false,"
                + "\"success\":true},\"stored\""
                + ":\"2021-05-04T23:30:40.014Z\","
                + "\"timestamp\":\"2021-05-04T23:30:40.014Z\","
                + "\"verb\":{\"display\"" + ":{\"en-US\":\"completed\"},"
                + "\"id\":\"http://adlnet.gov/expapi/verbs/completed\"}}");
        listStatments.add(elrrStatement);

        MockHttpServletRequestBuilder rquestBuilder = MockMvcRequestBuilders
                .get("/api/lrsdata?lastReadDate=2021-01-10T00:00:00Z")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = this.mockMvc.perform(rquestBuilder).andReturn();
        MockHttpServletResponse servletResponse = mvcResult.getResponse();
        String content = servletResponse.getContentAsString();
        HttpStatus status = HttpStatus.valueOf(servletResponse.getStatus());
        assertEquals(status.is2xxSuccessful(), true);
    }

    @Test
    void testlocalDataStatusResult() throws Exception {
        List<ElrrStatement> listStatments = new ArrayList<ElrrStatement>();
        ElrrStatement elrrStatement = new ElrrStatement();
        elrrStatement.setActor("mailto:c.cooper@yahoo.com");
        elrrStatement.setActorName("Cristopher Cunningham");
        elrrStatement.setVerb("http://adlnet.gov/expapi/verbs/completed");
        elrrStatement.setActivity(
                "http://adlnet.gov/expapi/activities/example/CLC%20104");
        elrrStatement.setCourseName("Analyzing Profit or Fee");
        elrrStatement.setStatementjson("{\"actor\":{\"mbox\""
                + ":\"mailto:c.cooper@yahoo.com\","
                + "\"name\":\"Cristopher Cunningham\",\"objectType\""
                + ":\"Agent\"},\"authority\":{\"account\":{\"homePage\""
                + ":\"https://deloitte-prototype-noisy.lrs.io/keys/"
                + "ELRR_Validation\","
                + "\"name\":\"ELRR_Validation\"},\"objectType\":"
                + "\"Agent\"},\"id\""
                + ":\"4859303f-8237-46d2-8bf3-186069514eb7\","
                + "\"object\":{\"definition\"" + ":{\"name\":{\"en-US\""
                + ":\"Analyzing Profit or Fee\"}},\"id\""
                + ":\"http://adlnet.gov/expapi/activities/example/CLC%20104\","
                + "\"objectType\":\"Activity\"},"
                + "\"result\":{\"completion\":false,"
                + "\"success\":true},\"stored\""
                + ":\"2021-05-04T23:30:40.014Z\","
                + "\"timestamp\":\"2021-05-04T23:30:40.014Z\","
                + "\"verb\":{\"display\"" + ":{\"en-US\":\"completed\"},"
                + "\"id\":\"http://adlnet.gov/expapi/verbs/completed\"}}");
        listStatments.add(elrrStatement);

        MockHttpServletRequestBuilder rquestBuilder = MockMvcRequestBuilders
                .get("/api/lrsdata?lastReadDate=2021-01-10T00:00:00Z")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = this.mockMvc.perform(rquestBuilder).andReturn();
        ObjectMapper mapper = new ObjectMapper();
        List<ElrrStatement> responseListStatments = mapper.
                readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<ElrrStatement>>() {
                });
        assertNotEquals(0, responseListStatments.size());
        MockHttpServletResponse servletResponse = mvcResult.getResponse();
        String content = servletResponse.getContentAsString();
        HttpStatus status = HttpStatus.valueOf(servletResponse.getStatus());
        assertEquals(status.is2xxSuccessful(), true);
    }

    @Test
    void testlocalDataNoResults() throws Exception {

        MockHttpServletRequestBuilder rquestBuilder = MockMvcRequestBuilders
                .get("/api/lrsdata?lastReadDate=2022-12-10T00:00:00Z")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = this.mockMvc.perform(rquestBuilder).andReturn();
        MockHttpServletResponse servletResponse = mvcResult.getResponse();
        ObjectMapper mapper = new ObjectMapper();
        List<ElrrStatement> responseListStatments = mapper.
                readValue(servletResponse.getContentAsString(),
                new TypeReference<List<ElrrStatement>>() {
                });
        assertEquals(0, responseListStatments.size());
        HttpStatus status = HttpStatus.valueOf(servletResponse.getStatus());
        assertEquals(status.is2xxSuccessful(), true);
    }

}
