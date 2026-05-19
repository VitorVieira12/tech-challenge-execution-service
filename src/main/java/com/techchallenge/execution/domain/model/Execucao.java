package com.techchallenge.execution.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "execucoes")
public class Execucao {

    @Id
    private String id;

    @Indexed(unique = true)
    private Long osId;

    private Long orcamentoId;
    private BigDecimal valorOrcamento;
    private StatusExecucao status = StatusExecucao.AGUARDANDO;
    private String tecnicoResponsavel;
    private String observacoes;
    private List<String> historicoStatus = new ArrayList<>();
    private LocalDateTime criadoEm = LocalDateTime.now();
    private LocalDateTime atualizadoEm;
    private LocalDateTime finalizadoEm;

    public Execucao() {}

    public Execucao(Long osId, Long orcamentoId, BigDecimal valorOrcamento) {
        this.osId = osId;
        this.orcamentoId = orcamentoId;
        this.valorOrcamento = valorOrcamento;
        this.historicoStatus.add("AGUARDANDO - " + LocalDateTime.now());
    }

    public void atualizarStatus(StatusExecucao novoStatus, String obs) {
        this.status = novoStatus;
        this.atualizadoEm = LocalDateTime.now();
        this.observacoes = obs;
        this.historicoStatus.add(novoStatus.name() + " - " + LocalDateTime.now() + (obs != null ? " - " + obs : ""));
        if (novoStatus == StatusExecucao.FINALIZADA || novoStatus == StatusExecucao.FALHOU) {
            this.finalizadoEm = LocalDateTime.now();
        }
    }

    // Getters e Setters
    public String getId() { return id; }
    public Long getOsId() { return osId; }
    public void setOsId(Long osId) { this.osId = osId; }
    public Long getOrcamentoId() { return orcamentoId; }
    public void setOrcamentoId(Long orcamentoId) { this.orcamentoId = orcamentoId; }
    public BigDecimal getValorOrcamento() { return valorOrcamento; }
    public void setValorOrcamento(BigDecimal valorOrcamento) { this.valorOrcamento = valorOrcamento; }
    public StatusExecucao getStatus() { return status; }
    public void setStatus(StatusExecucao status) { this.status = status; }
    public String getTecnicoResponsavel() { return tecnicoResponsavel; }
    public void setTecnicoResponsavel(String tecnicoResponsavel) { this.tecnicoResponsavel = tecnicoResponsavel; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public List<String> getHistoricoStatus() { return historicoStatus; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
    public LocalDateTime getFinalizadoEm() { return finalizadoEm; }
}
