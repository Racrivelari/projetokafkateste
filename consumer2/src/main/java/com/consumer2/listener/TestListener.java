package com.consumer2.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TestListener {
    
    @KafkaListener(topics = "topicoTeste", groupId = "group-1")
    public void listen(ConsumerRecord<String, String> message){
        // log.info("Thread: {}", Thread.currentThread().getId());
        // log.info(message);
        log.info("Mensagem: {} - Particao: {} |  Topico: {} ", 
            message.value(),
            message.partition(),
            message.topic());
    }


}
