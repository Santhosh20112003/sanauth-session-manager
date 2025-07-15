package com.example.demo.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Session {
    private String sessionId;
    private String subscriberEmail;
    private String deviceInfo;
    private String ipAddress;
    private LocalDateTime loginTime;
    private boolean active;

    public static Session create(String subscriberEmail, String deviceInfo, String ipAddress) {
        return new Session(UUID.randomUUID().toString(), subscriberEmail, deviceInfo, ipAddress, LocalDateTime.now(), true);
    }
} 