package com.example.demo.modal;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketPayload {
    private String email;
    private String type;
    private String token;
    private String message;
    private String sessionId;    // for revoke
    private String deviceInfo;   // for create
    private String ipAddress;    // for create
    private String location;     // for create
    private Instant loginTime;
} 