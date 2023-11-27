package com.producer.sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.producer.service.ProducerService;

@RestController
@RequestMapping("/mensagem")
public class Sender {

    @Autowired
    ProducerService producerService;

    @GetMapping("send")
    public void send() {
        producerService.sendMessage();
    }

    @PostMapping("send")
    public ResponseEntity<String> postMessage(@RequestBody String mensagem){
        producerService.sendMessage(mensagem);
        return ResponseEntity.ok().body("Mensagem enviada com sucesso: " + mensagem);
    }
}