package com.techchallenge.execution.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techchallenge.execution.domain.model.Execucao;
import com.techchallenge.execution.domain.model.StatusExecucao;
import com.techchallenge.execution.domain.service.ExecucaoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExecucaoController.class)
@ActiveProfiles("test")
class ExecucaoControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean ExecucaoService execucaoService;

    @Test
    @DisplayName("GET /execucoes deve retornar fila")
    void deveListarFila() throws Exception {
        Execucao e = new Execucao(1L, 10L, BigDecimal.valueOf(500));
        when(execucaoService.listarFila()).thenReturn(List.of(e));

        mockMvc.perform(get("/execucoes"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].osId").value(1));
    }

    @Test
    @DisplayName("GET /execucoes/status/AGUARDANDO deve retornar lista filtrada")
    void deveListarPorStatus() throws Exception {
        Execucao e = new Execucao(1L, 10L, BigDecimal.valueOf(500));
        when(execucaoService.listarPorStatus(StatusExecucao.AGUARDANDO)).thenReturn(List.of(e));

        mockMvc.perform(get("/execucoes/status/AGUARDANDO"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].status").value("AGUARDANDO"));
    }
}
