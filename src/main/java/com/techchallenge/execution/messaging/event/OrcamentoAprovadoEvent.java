package com.techchallenge.execution.messaging.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrcamentoAprovadoEvent(
    Long orcamentoId,
    Long osId,
    BigDecimal valor,
    LocalDateTime aprovadoEm
) {}
