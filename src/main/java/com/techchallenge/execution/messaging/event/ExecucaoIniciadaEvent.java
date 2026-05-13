package com.techchallenge.execution.messaging.event;

import java.time.LocalDateTime;

public record ExecucaoIniciadaEvent(
    String execucaoId,
    Long osId,
    Long orcamentoId,
    LocalDateTime iniciadoEm
) {}
