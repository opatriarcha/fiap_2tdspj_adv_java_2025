package br.com.fiap.javaadv.tdspj.spring.activemq.messaging;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class TopicConsumerB {

  @JmsListener(destination = "demo.topic", containerFactory = "topicListenerContainerFactory")
  public void onTopicMessageB(String body) {
    System.out.println("[TOPIC-B] Recebido: " + body);
  }
}
