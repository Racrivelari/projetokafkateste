package com.producer.service;

import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ProducerService {

    private static final Logger logger = LoggerFactory.getLogger(ProducerService.class);
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(){
        IntStream.range(1, 10)
            .boxed()
            .forEach(n -> kafkaTemplate.send("topicoTeste", "Mensagem: " + n));;
 
    }

    public void sendMessage(String message){
        logger.info("Mensagem -> {}", message);
        this.kafkaTemplate.send("topicoTeste", message);
 
    }

}
