package com.consumer.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TestListener {

    @Value("${api.python.url}") 
    private String apiUrl;

    private final RestTemplate restTemplate;

    public TestListener(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @KafkaListener(topics = "topicoTeste", groupId = "group-1")
    public void listen(ConsumerRecord<String, String> message) {
        log.info("Mensagem: {} - Particao: {} |  Topico: {} ",
                message.value(),
                message.partition(),
                message.topic());

        enviarMensagemParaAPI(message.value());
    }

    private void enviarMensagemParaAPI(String mensagem) {
    try {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(mensagem, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(apiUrl, requestEntity, String.class);

        log.info("Mensagem enviada com sucesso para a API Python. Resposta: {}", responseEntity.getBody());
    } catch (Exception e) {
        log.error("Erro ao enviar mensagem para a API Python. Detalhes: {}", e.getMessage());
    }
}
}
