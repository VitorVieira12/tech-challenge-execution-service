package com.techchallenge.execution.messaging.consumer;

import com.techchallenge.execution.config.RabbitMQConfig;
import com.techchallenge.execution.domain.service.ExecucaoService;
import com.techchallenge.execution.messaging.event.OrcamentoAprovadoEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class BillingEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(BillingEventConsumer.class);
    private final ExecucaoService execucaoService;

    public BillingEventConsumer(ExecucaoService execucaoService) {
        this.execucaoService = execucaoService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_EXEC_ORCAMENTO_APROVADO)
    public void consumeOrcamentoAprovado(OrcamentoAprovadoEvent event) {
        log.info("Evento orcamento.aprovado recebido: osId={}, orcamentoId={}", event.osId(), event.orcamentoId());
        execucaoService.iniciarExecucao(event);
    }
}
