package com.techchallenge.execution.domain.dto;

import com.techchallenge.execution.domain.model.Execucao;
import com.techchallenge.execution.domain.model.StatusExecucao;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ExecucaoResponseDTO(
    String id,
    Long osId,
    Long orcamentoId,
    BigDecimal valorOrcamento,
    StatusExecucao status,
    String tecnicoResponsavel,
    String observacoes,
    List<String> historicoStatus,
    LocalDateTime criadoEm,
    LocalDateTime atualizadoEm,
    LocalDateTime finalizadoEm
) {
    public static ExecucaoResponseDTO from(Execucao e) {
        return new ExecucaoResponseDTO(
            e.getId(), e.getOsId(), e.getOrcamentoId(), e.getValorOrcamento(),
            e.getStatus(), e.getTecnicoResponsavel(), e.getObservacoes(),
            e.getHistoricoStatus(), e.getCriadoEm(), e.getAtualizadoEm(), e.getFinalizadoEm()
        );
    }
}
