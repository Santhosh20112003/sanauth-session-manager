package com.example.demo.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class RevokeAccessWebController {

    @MessageMapping("/sendMessage") // Client sends to /app/sendMessage
    @SendTo("/revoke/{email}")      // Broadcasts to /topic/messages
    public String sendMessage(String message) {
        System.out.println("Received: " + message);
        return message;
    }
}