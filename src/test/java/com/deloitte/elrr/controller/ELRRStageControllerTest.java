/**
 *
 */
package com.deloitte.elrr.controller;

import com.deloitte.elrr.dto.ElrrStatement;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.adlnet.xapi.client.StatementClient;
import gov.adlnet.xapi.model.Statement;
import gov.adlnet.xapi.model.StatementResult;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author mnelakurti
 *
 */
@WebMvcTest(ELRRStageController.class)
class ELRRStageControllerTest {


    /**
    *
    */
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private StatementClient statementClient;

    @Mock
    private StatementResult statementResult;


    @Test
    void testlocalData() throws Exception {
        when(statementClient.filterBySince("2021-01-02T00:00:00Z"))
                .thenReturn(statementClient);
        when(statementClient.getStatements()).thenReturn(statementResult);
        when(statementResult.getStatements()).thenReturn(getStatmentsList());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/lrsdata?lastReadDate=2021-01-02T00:00:00Z")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse servletResponse = mvcResult.getResponse();
        assertEquals(null, servletResponse.getErrorMessage());
    }
    @Test
    void testlocalDataSize() throws Exception {
        when(statementClient.filterBySince("2022-12-10T00:00:00Z"))
                .thenReturn(statementClient);
        when(statementResult.getStatements()).thenReturn(getStatmentsList());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/lrsdata?lastReadDate=2022-12-10T00:00:00Z")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andDo(print());
        MvcResult mvcResult = this.mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse servletResponse = mvcResult.getResponse();
        assertEquals(null, servletResponse.getErrorMessage());
    }

    @Test
    void testlocalDataStatusOK() throws Exception {
        when(statementClient.filterBySince("2022-12-10T00:00:00Z"))
                .thenReturn(statementClient);
        when(statementResult.getStatements()).thenReturn(getStatmentsList());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/lrsdata?lastReadDate1=2022-12-10T00:00:00Z")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andDo(print());
        MvcResult mvcResult = this.mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse servletResponse = mvcResult.getResponse();
        assertEquals(null, servletResponse.getErrorMessage());
        ObjectMapper mapper = new ObjectMapper();
        List<ElrrStatement> responseListStatments = mapper.
                readValue(mvcResult.getResponse().getContentAsString(),
                        new TypeReference<List<ElrrStatement>>() {
                        });
        assertEquals(1, responseListStatments.size());
    }
    @Test
    void testlocalDataStatusResult() throws Exception {
        when(statementClient.getStatements()).thenReturn(statementResult);
        when(statementResult.getStatements()).thenReturn(getStatmentsList());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/lrsdatalastReadDate=202-12-10T00:00:00Z")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().is4xxClientError())
                .andDo(print());
        MvcResult mvcResult = this.mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse servletResponse = mvcResult.getResponse();
        assertEquals(null, servletResponse.getErrorMessage());
    }

    /**
     *
     * @return List<ElrrStatement>
     */
    private static  ArrayList<Statement> getStatmentsList() {
        ArrayList<Statement> listStatments = new ArrayList<Statement>();
        Statement statement = new Statement();
        listStatments.add(statement);
        return listStatments;
    }
}
