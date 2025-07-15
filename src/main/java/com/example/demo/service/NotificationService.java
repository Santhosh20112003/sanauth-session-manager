package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Sends a message to all WebSocket subscribers listening on /revoke/{email}.
     *
     * @param email     the email identifier for the topic
     * @param message   the message payload to send
     */
    public void sendRevokeMessage(String email, String message) {
        String destination = String.format("/revoke/%s", email);
        System.out.println("Sending to: " + destination + ", message: " + message);
        messagingTemplate.convertAndSend(destination, message);
    }

}
