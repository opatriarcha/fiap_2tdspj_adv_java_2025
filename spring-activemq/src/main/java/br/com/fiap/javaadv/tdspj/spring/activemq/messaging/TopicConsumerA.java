package br.com.fiap.javaadv.tdspj.spring.activemq.messaging;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class TopicConsumerA {

  @JmsListener(destination = "demo.topic", containerFactory = "topicListenerContainerFactory")
  public void onTopicMessageA(String body) {
    System.out.println("[TOPIC-A] Recebido: " + body);
  }
}
