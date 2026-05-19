package com.techchallenge.execution.domain.dto;

import com.techchallenge.execution.domain.model.StatusExecucao;
import jakarta.validation.constraints.NotNull;

public record StatusUpdateRequest(
    @NotNull StatusExecucao status,
    String tecnico,
    String observacoes
) {}
