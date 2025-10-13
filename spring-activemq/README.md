# Spring Boot 3.5.6 + ActiveMQ EMBUTIDO (memória)

Demonstra **filas** e **tópicos** usando um **broker ActiveMQ Classic embutido** via `BrokerService` — **sem instalar nada** além do próprio projeto.

## Requisitos
- Java 17+
- Maven 3.9+

## Rodando
```bash
mvn spring-boot:run
# ou
mvn -q -DskipTests package
java -jar target/spring-activemq-embedded-demo-1.0.0.jar
```

Você verá no console:
```
[QUEUE] Recebido: Mensagem inicial de FILA
[TOPIC-A] Recebido: Mensagem inicial de TÓPICO
[TOPIC-B] Recebido: Mensagem inicial de TÓPICO
```

## Teste por HTTP
```bash
# Envia 5 mensagens para a FILA
curl -X POST http://localhost:8080/api/send/queue/5

# Envia 3 mensagens para o TÓPICO
curl -X POST http://localhost:8080/api/send/topic/3
```

- Fila: apenas **um** consumidor processa cada mensagem (P2P).
- Tópico: **todos** os subscribers recebem todas as mensagens (Pub/Sub).

## Estrutura
- `EmbeddedActiveMQConfig` cria o broker em memória e define duas factories de listener (fila/tópico).
- `JmsConfig` expõe dois `JmsTemplate`s (fila/tópico).
- `QueueConsumer`, `TopicConsumerA`, `TopicConsumerB` consomem mensagens.
- `Producer` envia mensagens via `CommandLineRunner` e endpoints REST.
