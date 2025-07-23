package com.example.demo.modal;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisSessionModal {
	private String email;
	private String token;
	private String loginTime;
	private String deviceInfo;
	private String ipAddress;
	private String location;

}
