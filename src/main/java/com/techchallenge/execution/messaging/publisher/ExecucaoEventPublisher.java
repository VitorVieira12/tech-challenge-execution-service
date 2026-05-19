package com.techchallenge.execution.messaging.publisher;

import com.techchallenge.execution.config.RabbitMQConfig;
import com.techchallenge.execution.messaging.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ExecucaoEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(ExecucaoEventPublisher.class);
    private final RabbitTemplate rabbitTemplate;

    public ExecucaoEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishExecucaoIniciada(ExecucaoIniciadaEvent event) {
        log.info("Publicando execucao.iniciada para osId={}", event.osId());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXECUTION_EVENTS_EXCHANGE, RabbitMQConfig.RK_EXECUCAO_INICIADA, event);
    }

    public void publishExecucaoFinalizada(ExecucaoFinalizadaEvent event) {
        log.info("Publicando execucao.finalizada para osId={}", event.osId());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXECUTION_EVENTS_EXCHANGE, RabbitMQConfig.RK_EXECUCAO_FINALIZADA, event);
    }

    public void publishExecucaoFalhou(ExecucaoFalhouEvent event) {
        log.info("Publicando execucao.falhou para osId={}", event.osId());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXECUTION_EVENTS_EXCHANGE, RabbitMQConfig.RK_EXECUCAO_FALHOU, event);
    }
}
