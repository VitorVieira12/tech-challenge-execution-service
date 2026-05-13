package com.techchallenge.execution.domain.service;

import com.techchallenge.execution.domain.exception.BusinessException;
import com.techchallenge.execution.domain.exception.ResourceNotFoundException;
import com.techchallenge.execution.domain.model.Execucao;
import com.techchallenge.execution.domain.model.StatusExecucao;
import com.techchallenge.execution.domain.repository.ExecucaoRepository;
import com.techchallenge.execution.messaging.event.*;
import com.techchallenge.execution.messaging.publisher.ExecucaoEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExecucaoService {

    private static final Logger log = LoggerFactory.getLogger(ExecucaoService.class);
    private final ExecucaoRepository repository;
    private final ExecucaoEventPublisher publisher;

    public ExecucaoService(ExecucaoRepository repository, ExecucaoEventPublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    public void iniciarExecucao(OrcamentoAprovadoEvent event) {
        if (repository.findByOsId(event.osId()).isPresent()) {
            log.warn("Execução já existe para osId={}", event.osId());
            return;
        }
        Execucao execucao = new Execucao(event.osId(), event.orcamentoId(), event.valor());
        repository.save(execucao);

        publisher.publishExecucaoIniciada(new ExecucaoIniciadaEvent(
            execucao.getId(), event.osId(), event.orcamentoId(), LocalDateTime.now()
        ));
        log.info("Execução iniciada: id={}, osId={}", execucao.getId(), event.osId());
    }

    public List<Execucao> listarFila() {
        return repository.findAllByOrderByCriadoEmAsc();
    }

    public Execucao buscarPorId(String id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Execução não encontrada: " + id));
    }

    public Execucao buscarPorOsId(Long osId) {
        return repository.findByOsId(osId)
            .orElseThrow(() -> new ResourceNotFoundException("Execução não encontrada para OS: " + osId));
    }

    public List<Execucao> listarPorStatus(StatusExecucao status) {
        return repository.findByStatusOrderByCriadoEmAsc(status);
    }

    public Execucao atualizarStatus(Long osId, StatusExecucao novoStatus, String tecnico, String obs) {
        Execucao execucao = buscarPorOsId(osId);

        validarTransicaoStatus(execucao.getStatus(), novoStatus);
        execucao.setTecnicoResponsavel(tecnico);
        execucao.atualizarStatus(novoStatus, obs);
        repository.save(execucao);

        if (novoStatus == StatusExecucao.FINALIZADA) {
            publisher.publishExecucaoFinalizada(new ExecucaoFinalizadaEvent(
                execucao.getId(), osId, tecnico, obs, LocalDateTime.now()
            ));
        } else if (novoStatus == StatusExecucao.FALHOU) {
            publisher.publishExecucaoFalhou(new ExecucaoFalhouEvent(
                execucao.getId(), osId, obs, LocalDateTime.now()
            ));
        }
        return execucao;
    }

    private void validarTransicaoStatus(StatusExecucao atual, StatusExecucao novo) {
        if (atual == StatusExecucao.FINALIZADA || atual == StatusExecucao.FALHOU) {
            throw new BusinessException("Execução já foi encerrada com status: " + atual);
        }
    }
}
