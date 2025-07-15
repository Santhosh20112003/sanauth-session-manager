package com.example.demo.controller;

import com.example.demo.modal.Session;
import com.example.demo.modal.WebSocketPayload;
import com.example.demo.service.SessionService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class SessionWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final SessionService sessionService;

    public SessionWebSocketController(SimpMessagingTemplate messagingTemplate, SessionService sessionService) {
        this.messagingTemplate = messagingTemplate;
        this.sessionService = sessionService;
    }

    @MessageMapping("/session")
    public void handleSession(WebSocketPayload payload) {
        String email = payload.getEmail();
        switch (payload.getType()) {
            case "SESSION_CREATE":
                Session session = sessionService.createSession(email, payload.getDeviceInfo(), payload.getIpAddress());
                // Send confirmation to the user
                messagingTemplate.convertAndSendToUser(email, "/queue/reply",
                        new WebSocketPayload(email, "SESSION_CREATED", null, "Session created: " + session.getSessionId(), session.getSessionId(), session.getDeviceInfo(), session.getIpAddress()));
                break;
            case "SESSION_LIST":
                List<Session> sessions = sessionService.getSessions(email);
                // Send the list back to the user
                messagingTemplate.convertAndSendToUser(email, "/queue/reply",
                        new WebSocketPayload(email, "SESSION_LIST", null, sessions.toString(), null, null, null));
                break;
            case "SESSION_REVOKE":
                boolean revoked = sessionService.revokeSession(email, payload.getSessionId());
                messagingTemplate.convertAndSendToUser(email, "/queue/reply",
                        new WebSocketPayload(email, revoked ? "SESSION_REVOKED" : "SESSION_REVOKE_FAILED", null, "Session revoke: " + payload.getSessionId(), payload.getSessionId(), null, null));
                break;
        }
    }

    // For broadcasting updates to all devices (used by SessionService)
    public void sendSessionUpdate(String email, WebSocketPayload payload) {
        messagingTemplate.convertAndSend("/topic/sessions/" + email, payload);
    }
} 