package com.techchallenge.execution.messaging.event;

import java.time.LocalDateTime;

public record ExecucaoFalhouEvent(
    String execucaoId,
    Long osId,
    String motivo,
    LocalDateTime falhouEm
) {}
