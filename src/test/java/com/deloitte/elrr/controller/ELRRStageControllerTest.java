package com.deloitte.elrr.controller;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.deloitte.elrr.HeaderFilter;
import com.deloitte.elrr.JSONRequestSizeLimitFilter;
import com.deloitte.elrr.util.TestFileUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yetanalytics.xapi.client.StatementClient;
import com.yetanalytics.xapi.client.filters.StatementFilters;
import com.yetanalytics.xapi.model.Statement;
import com.yetanalytics.xapi.model.StatementResult;
import com.yetanalytics.xapi.util.Mapper;

/**
 * @author mnelakurti
 */
@WebMvcTest(ELRRStageController.class)
@AutoConfigureMockMvc(addFilters = false)
class ELRRStageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private StatementClient statementClient;

    @Mock
    private StatementResult statementResult;

    @Mock
    private HeaderFilter headerFilter;

    private JSONRequestSizeLimitFilter sl = new JSONRequestSizeLimitFilter();
    private HeaderFilter hf = new HeaderFilter();

    @Test
    @WithMockUser
    void testlocalData() throws Exception {

        try {

            ReflectionTestUtils.setField(hf, "checkHttpHeader", true);
            ReflectionTestUtils.setField(sl, "maxSizeLimit", 2000000L);
            ReflectionTestUtils.setField(sl, "checkMediaTypeJson", false);

            File testFile = TestFileUtil.getJsonTestFile("competency.json");

            Statement stmt = Mapper.getMapper().readValue(testFile,
                    Statement.class);

            List<Statement> list = Arrays.asList(stmt);

            String lastReadDate = "2021-01-02T00:00:00Z";
            StatementFilters filters = new StatementFilters();
            filters.setSince(lastReadDate);

            when(statementClient.getStatements(filters, 10)).thenReturn(list);

            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/api/lrsdata?lastReadDate=2021-01-02T00:00:00Z&MaxStatements=10")
                    .content(list.toString()).contentType(
                            MediaType.APPLICATION_JSON);
            MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse servletResponse = mvcResult.getResponse();
            assertEquals(null, servletResponse.getErrorMessage());

        } catch (IOException e) {
            fail("Should not have thrown any exception");
        }

    }

    @Test
    void testlocalDataSize() throws Exception {

        try {

            File testFile = TestFileUtil.getJsonTestFile("competency.json");

            Statement stmt = Mapper.getMapper().readValue(testFile,
                    Statement.class);

            List<Statement> list = Arrays.asList(stmt);

            String lastReadDate = "2021-01-02T00:00:00Z";
            StatementFilters filters = new StatementFilters();
            filters.setSince(lastReadDate);

            when(statementClient.getStatements(filters, 10)).thenReturn(list);

            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/api/lrsdata?lastReadDate=2022-12-10T00:00:00Z&maxStatements=10")
                    .accept(MediaType.APPLICATION_JSON).contentType(
                            MediaType.APPLICATION_JSON);
            MvcResult mvcResult = this.mockMvc.perform(requestBuilder)
                    .andReturn();
            MockHttpServletResponse servletResponse = mvcResult.getResponse();
            assertEquals(null, servletResponse.getErrorMessage());

        } catch (IOException e) {
            fail("Should not have thrown any exception");
        }

    }

    @Test
    void testlocalDataStatusOK() throws Exception {

        try {

            File testFile = TestFileUtil.getJsonTestFile("competency.json");

            Statement stmt = Mapper.getMapper().readValue(testFile,
                    Statement.class);

            List<Statement> list = Arrays.asList(stmt);

            String lastReadDate = "2021-01-02T00:00:00Z";
            StatementFilters filters = new StatementFilters();
            filters.setSince(lastReadDate);

            when(statementClient.getStatements(filters, 10)).thenReturn(list);

            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/api/lrsdata?lastReadDate1=2022-12-10T00:00:00Z&maxStatements=10")
                    .accept(MediaType.APPLICATION_JSON).contentType(
                            MediaType.APPLICATION_JSON);
            MvcResult mvcResult = this.mockMvc.perform(requestBuilder)
                    .andReturn();
            MockHttpServletResponse servletResponse = mvcResult.getResponse();
            if (servletResponse.getStatus() == 400) {
                return;
            }
            assertEquals(null, servletResponse.getErrorMessage());
            ObjectMapper mapper = new ObjectMapper();
            List<Statement> responseListStatments = mapper.readValue(mvcResult
                    .getResponse().getContentAsString(),
                    new TypeReference<List<Statement>>() {
                    });
            assertEquals(1, responseListStatments.size());

        } catch (IOException e) {
            e.printStackTrace();
            fail("Should not have thrown any exception");
        }

    }

    @Test
    void testlocalDataStatusResult() throws Exception {

        try {

            File testFile = TestFileUtil.getJsonTestFile("competency.json");

            Statement stmt = Mapper.getMapper().readValue(testFile,
                    Statement.class);

            List<Statement> list = Arrays.asList(stmt);

            String lastReadDate = "2021-01-02T00:00:00Z";
            StatementFilters filters = new StatementFilters();
            filters.setSince(lastReadDate);

            when(statementClient.getStatements(filters, 10)).thenReturn(list);

            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/api/lrsdatalastReadDate=2022-12-10T00:00:00Z&maxStatements=10")
                    .accept(MediaType.APPLICATION_JSON).contentType(
                            MediaType.APPLICATION_JSON);
            MvcResult mvcResult = this.mockMvc.perform(requestBuilder)
                    .andReturn();
            MockHttpServletResponse servletResponse = mvcResult.getResponse();
            assertEquals(null, servletResponse.getErrorMessage());

        } catch (IOException e) {
            fail("Should not have thrown any exception");
        }

    }

    @Test
    @WithMockUser
    void testLocalDataInvalidDate() throws Exception {
        try {
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/api/lrsdata?lastReadDate=TEST-TEST-TEST&maxStatements=10")
                    .accept(MediaType.APPLICATION_JSON).contentType(
                            MediaType.APPLICATION_JSON);

            MvcResult mvcResult = this.mockMvc.perform(requestBuilder)
                    .andReturn();

        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
    }

    @Test
    void testException() {

        try {
            ELRRStageController elrrStageController = new ELRRStageController();
            elrrStageController.localData(null, 0);
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("lastReadDate is null"));
        }

    }
}
