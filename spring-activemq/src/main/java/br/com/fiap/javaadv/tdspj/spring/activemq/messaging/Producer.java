package br.com.fiap.javaadv.tdspj.spring.activemq.messaging;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
@RestController
@RequestMapping("/api")
public class Producer implements CommandLineRunner {

    private final JmsTemplate queueJmsTemplate;
    private final JmsTemplate topicJmsTemplate;

    public Producer(
            @Qualifier("queueJmsTemplate") JmsTemplate queueJmsTemplate,
            @Qualifier("topicJmsTemplate") JmsTemplate topicJmsTemplate) {
        this.queueJmsTemplate = queueJmsTemplate;
        this.topicJmsTemplate = topicJmsTemplate;
    }

  @GetMapping("/ping")
  public ResponseEntity<String> ping() {
    return ResponseEntity.ok("ok");
  }

  @PostMapping("/send/queue/{n}")
  public ResponseEntity<String> sendQueue(@PathVariable int n) {
    for (int i = 1; i <= n; i++) {
      queueJmsTemplate.convertAndSend("demo.queue", "Mensagem Fila #" + i);
    }
    return ResponseEntity.ok("Enviado " + n + " mensagens para demo.queue");
  }

  @PostMapping("/send/topic/{n}")
  public ResponseEntity<String> sendTopic(@PathVariable int n) {
    for (int i = 1; i <= n; i++) {
      topicJmsTemplate.convertAndSend("demo.topic", "Mensagem Tópico #" + i);
    }
    return ResponseEntity.ok("Enviado " + n + " mensagens para demo.topic");
  }

  @Override
  public void run(String... args) {
    queueJmsTemplate.convertAndSend("demo.queue", "Mensagem inicial de FILA");
    topicJmsTemplate.convertAndSend("demo.topic", "Mensagem inicial de TÓPICO");
  }
}
