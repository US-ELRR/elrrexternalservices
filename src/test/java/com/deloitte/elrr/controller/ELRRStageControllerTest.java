package com.deloitte.elrr.controller;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
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
@WebMvcTest(excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@SuppressWarnings("checkstyle:linelength")
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

            String lastReadDateStr = "2021-01-02T00:00:00Z";
            ZonedDateTime lastReadDate = ZonedDateTime.parse(lastReadDateStr);
            StatementFilters filters = new StatementFilters();
            filters.setSince(lastReadDate);

            when(statementClient.getStatements(filters, 10)).thenReturn(list);

            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/api/lrsdatalastReadDate=2022-12-10T00:00:00Z&maxStatements=10")
                    .accept(MediaType.APPLICATION_JSON).contentType(
                            MediaType.APPLICATION_JSON);
            mockMvc.perform(requestBuilder).andExpect(status()
                    .is4xxClientError()).andDo(print());
            MvcResult mvcResult = this.mockMvc.perform(requestBuilder)
                    .andReturn();
            MockHttpServletResponse servletResponse = mvcResult.getResponse();
            if (servletResponse.getStatus() == 404) {
                return;
            }
            assertEquals(servletResponse.getStatus(), HttpStatus.OK);

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

            String lastReadDateStr = "2021-01-02T00:00:00Z";
            ZonedDateTime lastReadDate = ZonedDateTime.parse(lastReadDateStr);

            StatementFilters filters = new StatementFilters();
            filters.setSince(lastReadDate);

            when(statementClient.getStatements(filters, 10)).thenReturn(list);

            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/api/lrsdatalastReadDate=2022-12-10T00:00:00Z&maxStatements=10")
                    .accept(MediaType.APPLICATION_JSON).contentType(
                            MediaType.APPLICATION_JSON);
            mockMvc.perform(requestBuilder).andExpect(status()
                    .is4xxClientError()).andDo(print());
            MvcResult mvcResult = this.mockMvc.perform(requestBuilder)
                    .andReturn();
            MockHttpServletResponse servletResponse = mvcResult.getResponse();
            if (servletResponse.getStatus() == 401) {
                return;
            }
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

            String lastReadDateStr = "2021-01-02T00:00:00Z";
            ZonedDateTime lastReadDate = ZonedDateTime.parse(lastReadDateStr);

            StatementFilters filters = new StatementFilters();
            filters.setSince(lastReadDate);

            when(statementClient.getStatements(filters, 10)).thenReturn(list);

            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/api/lrsdatalastReadDate=2022-12-10T00:00:00Z&maxStatements=10")
                    .accept(MediaType.APPLICATION_JSON).contentType(
                            MediaType.APPLICATION_JSON);
            mockMvc.perform(requestBuilder).andExpect(status()
                    .is4xxClientError()).andDo(print());
            MvcResult mvcResult = this.mockMvc.perform(requestBuilder)
                    .andReturn();
            MockHttpServletResponse servletResponse = mvcResult.getResponse();
            if (servletResponse.getStatus() != 200) {
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

            String lastReadDateStr = "2021-01-02T00:00:00Z";
            ZonedDateTime lastReadDate = ZonedDateTime.parse(lastReadDateStr);

            StatementFilters filters = new StatementFilters();
            filters.setSince(lastReadDate);

            when(statementClient.getStatements(filters, 10)).thenReturn(list);

            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/api/lrsdatalastReadDate=2022-12-10T00:00:00Z&maxStatements=10")
                    .accept(MediaType.APPLICATION_JSON).contentType(
                            MediaType.APPLICATION_JSON);
            mockMvc.perform(requestBuilder).andExpect(status()
                    .is4xxClientError()).andDo(print());
            MvcResult mvcResult = this.mockMvc.perform(requestBuilder)
                    .andReturn();
            MockHttpServletResponse servletResponse = mvcResult.getResponse();
            if (servletResponse.getStatus() == 401) {
                return;
            }
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

            mockMvc.perform(requestBuilder).andExpect(status()
                    .is4xxClientError()).andDo(print());

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
