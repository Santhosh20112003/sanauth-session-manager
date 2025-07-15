package com.example.demo.service;

import com.example.demo.controller.SessionWebSocketController;
import com.example.demo.modal.Session;
import com.example.demo.modal.WebSocketPayload;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionService {

    // In-memory session store (replace with DB in production)
    private final Map<String, List<Session>> sessionsBySubscriber = new ConcurrentHashMap<>();
    private final SessionWebSocketController wsController;

    public SessionService(SessionWebSocketController wsController) {
        this.wsController = wsController;
    }

    // Create a new session
    public Session createSession(String subscriberEmail, String deviceInfo, String ipAddress) {
        Session session = Session.create(subscriberEmail, deviceInfo, ipAddress);
        sessionsBySubscriber.computeIfAbsent(subscriberEmail, k -> new ArrayList<>()).add(session);

        // Notify subscriber via WebSocket
        wsController.sendSessionUpdate(subscriberEmail, new WebSocketPayload(
                subscriberEmail, "SESSION_CREATED", null, "New session created: " + session.getSessionId(), session.getSessionId(), deviceInfo, ipAddress
        ));

        return session;
    }

    // List all active sessions for a subscriber
    public List<Session> getSessions(String subscriberEmail) {
        return sessionsBySubscriber.getOrDefault(subscriberEmail, Collections.emptyList());
    }

    // Revoke a session
    public boolean revokeSession(String subscriberEmail, String sessionId) {
        List<Session> sessions = sessionsBySubscriber.get(subscriberEmail);
        if (sessions != null) {
            for (Session session : sessions) {
                if (session.getSessionId().equals(sessionId) && session.isActive()) {
                    session.setActive(false);

                    // Notify subscriber via WebSocket
                    wsController.sendSessionUpdate(subscriberEmail, new WebSocketPayload(
                            subscriberEmail, "SESSION_REVOKED", null, "Session revoked: " + sessionId, sessionId, session.getDeviceInfo(), session.getIpAddress()
                    ));
                    return true;
                }
            }
        }
        return false;
    }
} 