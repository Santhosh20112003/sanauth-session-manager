package com.example.demo.controller;

import com.example.demo.modal.Session;
import com.example.demo.modal.WebSocketPayload;
import com.example.demo.service.SessionService;
import org.springframework.context.event.EventListener;
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

//    @MessageMapping("/session")
//    public void handleSession(WebSocketPayload payload) {
//        String email = payload.getEmail();
//        switch (payload.getType()) {
//            case "SESSION_CREATE":
//                Session session = sessionService.createSession(email, payload.getDeviceInfo(), payload.getIpAddress());
//                // Optional: immediate reply (though the event listener will also handle broadcast)
//                messagingTemplate.convertAndSendToUser(email, "/queue/reply",
//                        new WebSocketPayload(email, "SESSION_CREATED", null, "Session created: " + session.getSessionId(), session.getSessionId(), session.getDeviceInfo(), session.getIpAddress()));
//                break;
//
//            case "SESSION_LIST":
//                List<Session> sessions = sessionService.getSessions(email);
//                messagingTemplate.convertAndSendToUser(email, "/queue/reply",
//                        new WebSocketPayload(email, "SESSION_LIST", null, sessions.toString(), null, null, null));
//                break;
//
//            case "SESSION_REVOKE":
//                boolean revoked = sessionService.revokeSession(email, payload.getSessionId());
//                messagingTemplate.convertAndSendToUser(email, "/queue/reply",
//                        new WebSocketPayload(email, revoked ? "SESSION_REVOKED" : "SESSION_REVOKE_FAILED", null, "Session revoke: " + payload.getSessionId(), payload.getSessionId(), null, null));
//                break;
//        }
//    }

    @EventListener
    public void handleSessionUpdate(WebSocketPayload payload) {
        // Send to topic for all devices of the user
        messagingTemplate.convertAndSend("/topic/sessions/" + payload.getEmail(), payload);
    }

    // Optional: still available if you want to call manually
    public void sendSessionUpdate(String email, WebSocketPayload payload) {
        messagingTemplate.convertAndSend("/topic/sessions/" + email, payload);
    }
}
