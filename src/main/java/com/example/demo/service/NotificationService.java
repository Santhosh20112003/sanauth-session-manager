package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.modal.RedisSessionModal;
import com.example.demo.modal.SessionRequestPayload;

@Service
public class NotificationService {

	private final SimpMessagingTemplate messagingTemplate;

	@Autowired
	public NotificationService(SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}

	@Autowired
	private RedisService redisService;

	public void sendRevokeMessage(String email, String message) {
		String destination = String.format("/revoke/%s", email);
		System.out.println("Sending to: " + destination + ", message: " + message);
		messagingTemplate.convertAndSend(destination, message);
	}

	public ResponseEntity<Object> publishSessionRequest(String email, String jwtToken,
			SessionRequestPayload sessionRequestPayload) {
		try {
			
			String key = String.format("%s_%s", email, jwtToken);
			
			RedisSessionModal redisSession = new RedisSessionModal(
					email, jwtToken, sessionRequestPayload.getLoginTime().toString(),
					sessionRequestPayload.getDeviceInfo(), sessionRequestPayload.getIpAddress(),
					sessionRequestPayload.getLocation());

			return new ResponseEntity<>(redisService.save(key,redisSession), HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error publishing session request: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
