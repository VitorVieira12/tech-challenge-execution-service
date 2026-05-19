package com.techchallenge.execution.service;

import com.techchallenge.execution.domain.exception.BusinessException;
import com.techchallenge.execution.domain.exception.ResourceNotFoundException;
import com.techchallenge.execution.domain.model.Execucao;
import com.techchallenge.execution.domain.model.StatusExecucao;
import com.techchallenge.execution.domain.repository.ExecucaoRepository;
import com.techchallenge.execution.domain.service.ExecucaoService;
import com.techchallenge.execution.messaging.event.OrcamentoAprovadoEvent;
import com.techchallenge.execution.messaging.publisher.ExecucaoEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExecucaoServiceTest {

    @Mock ExecucaoRepository repository;
    @Mock ExecucaoEventPublisher publisher;
    @InjectMocks ExecucaoService service;

    private Execucao execucao;

    @BeforeEach
    void setUp() {
        execucao = new Execucao(1L, 10L, BigDecimal.valueOf(500));
    }

    @Test
    @DisplayName("Deve iniciar execução ao receber orcamento.aprovado")
    void deveIniciarExecucao() {
        OrcamentoAprovadoEvent event = new OrcamentoAprovadoEvent(10L, 1L, BigDecimal.valueOf(500), LocalDateTime.now());
        when(repository.findByOsId(1L)).thenReturn(Optional.empty());
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        service.iniciarExecucao(event);

        verify(repository).save(any(Execucao.class));
        verify(publisher).publishExecucaoIniciada(any());
    }

    @Test
    @DisplayName("Não deve iniciar execução duplicada")
    void naoDeveIniciarExecucaoDuplicada() {
        OrcamentoAprovadoEvent event = new OrcamentoAprovadoEvent(10L, 1L, BigDecimal.valueOf(500), LocalDateTime.now());
        when(repository.findByOsId(1L)).thenReturn(Optional.of(execucao));

        service.iniciarExecucao(event);

        verify(repository, never()).save(any());
        verify(publisher, never()).publishExecucaoIniciada(any());
    }

    @Test
    @DisplayName("Deve atualizar status para FINALIZADA e publicar evento")
    void deveFinalizarExecucao() {
        when(repository.findByOsId(1L)).thenReturn(Optional.of(execucao));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Execucao resultado = service.atualizarStatus(1L, StatusExecucao.FINALIZADA, "Tecnico A", "Serviço concluído");

        assertThat(resultado.getStatus()).isEqualTo(StatusExecucao.FINALIZADA);
        verify(publisher).publishExecucaoFinalizada(any());
    }

    @Test
    @DisplayName("Deve atualizar status para FALHOU e publicar evento")
    void deveFalharExecucao() {
        when(repository.findByOsId(1L)).thenReturn(Optional.of(execucao));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Execucao resultado = service.atualizarStatus(1L, StatusExecucao.FALHOU, "Tecnico A", "Peça faltante");

        assertThat(resultado.getStatus()).isEqualTo(StatusExecucao.FALHOU);
        verify(publisher).publishExecucaoFalhou(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar status de execução encerrada")
    void deveLancarExcecaoStatusEncerrado() {
        execucao.atualizarStatus(StatusExecucao.FINALIZADA, "Concluído");
        when(repository.findByOsId(1L)).thenReturn(Optional.of(execucao));

        assertThatThrownBy(() -> service.atualizarStatus(1L, StatusExecucao.EM_EXECUCAO, "T", "obs"))
            .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar execução inexistente")
    void deveLancarExcecaoNaoEncontrada() {
        when(repository.findByOsId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorOsId(99L))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Deve listar fila de execuções")
    void deveListarFila() {
        when(repository.findAllByOrderByCriadoEmAsc()).thenReturn(List.of(execucao));

        List<Execucao> fila = service.listarFila();

        assertThat(fila).hasSize(1);
    }

    @Test
    @DisplayName("Deve listar execuções por status")
    void deveListarPorStatus() {
        when(repository.findByStatusOrderByCriadoEmAsc(StatusExecucao.AGUARDANDO)).thenReturn(List.of(execucao));

        List<Execucao> resultado = service.listarPorStatus(StatusExecucao.AGUARDANDO);

        assertThat(resultado).hasSize(1);
    }
}
