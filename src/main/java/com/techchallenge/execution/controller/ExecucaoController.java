package com.techchallenge.execution.controller;

import com.techchallenge.execution.domain.dto.ExecucaoResponseDTO;
import com.techchallenge.execution.domain.dto.StatusUpdateRequest;
import com.techchallenge.execution.domain.model.StatusExecucao;
import com.techchallenge.execution.domain.service.ExecucaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/execucoes")
@Tag(name = "Execuções", description = "Gerenciamento da fila de execução")
public class ExecucaoController {

    private final ExecucaoService service;

    public ExecucaoController(ExecucaoService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar fila de execução ordenada por data")
    public List<ExecucaoResponseDTO> listarFila() {
        return service.listarFila().stream().map(ExecucaoResponseDTO::from).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar execução por ID")
    public ExecucaoResponseDTO buscarPorId(@PathVariable String id) {
        return ExecucaoResponseDTO.from(service.buscarPorId(id));
    }

    @GetMapping("/os/{osId}")
    @Operation(summary = "Buscar execução pela OS")
    public ExecucaoResponseDTO buscarPorOsId(@PathVariable Long osId) {
        return ExecucaoResponseDTO.from(service.buscarPorOsId(osId));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Listar execuções por status")
    public List<ExecucaoResponseDTO> listarPorStatus(@PathVariable StatusExecucao status) {
        return service.listarPorStatus(status).stream().map(ExecucaoResponseDTO::from).toList();
    }

    @PatchMapping("/os/{osId}/status")
    @Operation(summary = "Atualizar status da execução")
    public ExecucaoResponseDTO atualizarStatus(@PathVariable Long osId, @RequestBody @Valid StatusUpdateRequest request) {
        return ExecucaoResponseDTO.from(
            service.atualizarStatus(osId, request.status(), request.tecnico(), request.observacoes())
        );
    }
}
