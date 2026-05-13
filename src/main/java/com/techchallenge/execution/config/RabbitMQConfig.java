package com.techchallenge.execution.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // ── Exchanges ────────────────────────────────────────────────────────────
    public static final String BILLING_EVENTS_EXCHANGE   = "billing.events";
    public static final String EXECUTION_EVENTS_EXCHANGE = "execution.events";

    // ── Routing keys ─────────────────────────────────────────────────────────
    public static final String RK_ORCAMENTO_APROVADO  = "orcamento.aprovado";
    public static final String RK_EXECUCAO_INICIADA   = "execucao.iniciada";
    public static final String RK_EXECUCAO_FINALIZADA = "execucao.finalizada";
    public static final String RK_EXECUCAO_FALHOU     = "execucao.falhou";

    // ── Queues ────────────────────────────────────────────────────────────────
    public static final String QUEUE_EXEC_ORCAMENTO_APROVADO = "exec.orcamento.aprovado";

    @Bean TopicExchange billingEventsExchange()    { return new TopicExchange(BILLING_EVENTS_EXCHANGE); }
    @Bean TopicExchange executionEventsExchange()  { return new TopicExchange(EXECUTION_EVENTS_EXCHANGE); }

    @Bean Queue execOrcamentoAprovadoQueue() {
        return QueueBuilder.durable(QUEUE_EXEC_ORCAMENTO_APROVADO).build();
    }

    @Bean Binding bindingExecOrcamentoAprovado(Queue execOrcamentoAprovadoQueue, TopicExchange billingEventsExchange) {
        return BindingBuilder.bind(execOrcamentoAprovadoQueue).to(billingEventsExchange).with(RK_ORCAMENTO_APROVADO);
    }

    @Bean
    Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory cf, Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(cf);
        template.setMessageConverter(converter);
        return template;
    }
}
