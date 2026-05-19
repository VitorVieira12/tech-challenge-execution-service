# Execution Service

Microsserviço de Execução e Produção — FIAP Tech Challenge Fase 4

## Responsabilidades
- Gerenciar fila de execução das OS
- Atualizar status durante diagnóstico e reparos
- Comunicar finalização (ou falha) ao OS Service

## Tecnologias
- Java 21 + Spring Boot 3.3.5
- MongoDB 7 (NoSQL)
- RabbitMQ (AMQP)
- Docker + Kubernetes

## Saga Pattern — Coreografado
Eventos consumidos: `orcamento.aprovado`
Eventos publicados: `execucao.iniciada`, `execucao.finalizada`, `execucao.falhou`

## Executar localmente
```bash
docker-compose up -d
mvn spring-boot:run
```

## Swagger
http://localhost:8082/swagger-ui.html

## Testes
```bash
mvn test
```
Cobertura mínima: 80% (JaCoCo)
