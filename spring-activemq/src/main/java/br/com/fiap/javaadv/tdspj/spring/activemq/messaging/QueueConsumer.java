package br.com.fiap.javaadv.tdspj.spring.activemq.messaging;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class QueueConsumer {

  @JmsListener(destination = "demo.queue", containerFactory = "jmsListenerContainerFactory")
  public void onQueueMessage(String body) {
    System.out.println("[QUEUE] Recebido: " + body);
  }
}
