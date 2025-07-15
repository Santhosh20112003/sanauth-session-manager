package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.modal.WebSocketPayload;
import com.example.demo.service.RedisService;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class MessageController {

    @Autowired
    private RedisService redisService;

    /**
     * Publish a message to WebSocket topic & save to Redis
     */
    @MessageMapping("/publish/{email}") // Maps to /app/publish/{email}
    @SendTo("/topic/notifications/{email}") // Broadcasts to /notification/{email}
    public WebSocketPayload publishMessage(WebSocketPayload payload, @DestinationVariable String email) {
        log.info("Publishing message for [{}]: {}", email, payload);

        String key = payload.getEmail() + "_" + payload.getToken();

        if (redisService.keyExists(key)) {
            log.warn("Key already exists in Redis: {}", key);
            return null; // no broadcast
        }

        try {
            redisService.save(key, payload);
            log.info("Payload saved to Redis with key: {}", key);
        } catch (Exception e) {
            log.error("Error saving payload to Redis: {}", e.getMessage(), e);
            return null;
        }

        log.info("Broadcasting message to /notification/{}", email);
        return payload;
    }

    /**
     * Get all active sessions for an email
     */
    // Maps to /sessions/{email}
    // Returns a list of WebSocketPayload objects for the given email
    // This method retrieves all active WebSocket sessions for a specific email from Redis.
    @GetMapping("/sessions/{email}")
    public List<WebSocketPayload> getActiveSessions(@PathVariable String email) {
        log.info("Fetching all active sessions for email: {}", email);

        String keyPattern = email + "_*";

        List<WebSocketPayload> activeSessions = redisService.getAllByPattern(keyPattern);

        if (activeSessions.isEmpty()) {
            log.warn("No active sessions found for email: {}", email);
        } else {
            log.info("Found {} active sessions for email: {}", activeSessions.size(), email);
        }

        return activeSessions;
    }
}
