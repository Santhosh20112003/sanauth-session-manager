package com.example.demo.service;

import com.example.demo.modal.Session;
import com.example.demo.modal.WebSocketPayload;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionService {

    private final Map<String, List<Session>> sessionsBySubscriber = new ConcurrentHashMap<>();
    private final ApplicationEventPublisher eventPublisher;

    public SessionService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

//    public Session createSession(String subscriberEmail, String deviceInfo, String ipAddress) {
//        Session session = Session.create(subscriberEmail, deviceInfo, ipAddress);
//        sessionsBySubscriber.computeIfAbsent(subscriberEmail, k -> new ArrayList<>()).add(session);
//
//        WebSocketPayload payload = new WebSocketPayload(
//				subscriberEmail, "SESSION_CREATED", null,
//				"Session created: " + session.getSessionId(),
//				session.getSessionId(), deviceInfo, ipAddress, session.ge, session.getLoginTime()
//		);
//       
//        eventPublisher.publishEvent(payload);
//
//        return session;
//    }
//
//    public List<Session> getSessions(String subscriberEmail) {
//        return sessionsBySubscriber.getOrDefault(subscriberEmail, Collections.emptyList());
//    }

//    public boolean revokeSession(String subscriberEmail, String sessionId) {
//        List<Session> sessions = sessionsBySubscriber.get(subscriberEmail);
//        if (sessions != null) {
//            for (Session session : sessions) {
//                if (session.getSessionId().equals(sessionId) && session.isActive()) {
//                    session.setActive(false);
//
//                    WebSocketPayload payload = new WebSocketPayload(
//                            subscriberEmail, "SESSION_REVOKED", null,
//                            "Session revoked: " + sessionId,
//                            sessionId, session.getDeviceInfo(), session.getIpAddress(),session.getLocation(), session.getLoginTime()
//                    );
//                    eventPublisher.publishEvent(payload);
//
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
}
