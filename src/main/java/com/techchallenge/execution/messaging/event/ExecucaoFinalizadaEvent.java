package com.techchallenge.execution.messaging.event;

import java.time.LocalDateTime;

public record ExecucaoFinalizadaEvent(
    String execucaoId,
    Long osId,
    String tecnicoResponsavel,
    String observacoes,
    LocalDateTime finalizadoEm
) {}
