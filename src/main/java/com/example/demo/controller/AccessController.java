package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.modal.SessionRequestPayload;
import com.example.demo.service.NotificationService;
import com.example.demo.service.RedisService;

@RestController
@RequestMapping("/api/session")
public class AccessController {

	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private RedisService redisService;

	@GetMapping("/revoke/{sessionId}")
	public ResponseEntity<String> revokeAccess(Authentication authentication,
			@PathVariable("sessionId") String sessionId) {
		String email = authentication.getName();
		
		if (sessionId == null || sessionId.isEmpty()) {
			return new ResponseEntity<>("Invalid session ID", HttpStatus.BAD_REQUEST);
		}
		
		redisService.deleteSession(sessionId, email);
		
		notificationService.sendRevokeMessage(email, sessionId);
		return new ResponseEntity<>("Access revoked for session: " + sessionId + " for user: " + email, HttpStatus.OK);
	}

	@PostMapping("/publish")
	public ResponseEntity<Object> publishSession(@RequestBody SessionRequestPayload sessionRequestPayload,
			Authentication authentication) {
		String jwtToken = authentication.getCredentials() instanceof String ? authentication.getCredentials().toString()
				: null;
		
		String email = authentication.getName();
		
		if (jwtToken == null || jwtToken.isEmpty()) {
			return new ResponseEntity<>("Invalid JWT token", HttpStatus.UNAUTHORIZED);
		}
		
		if (email == null || email.isEmpty()) {
			return new ResponseEntity<>("Invalid email", HttpStatus.UNAUTHORIZED);
		}
		
		System.out.println("Publishing session request for email: " + email + " with JWT token: " + jwtToken+" payload");
		System.out.println(	"Session Request Payload: " + sessionRequestPayload);

		return new ResponseEntity<>(notificationService.publishSessionRequest(email,jwtToken,sessionRequestPayload), HttpStatus.OK);
	}
	
	
	@PostMapping("/list/all")
	public ResponseEntity<Object> listAllSessions(Authentication authentication) {
		String email = authentication.getName();
		
		if (email == null || email.isEmpty()) {
			return new ResponseEntity<>("Invalid email", HttpStatus.UNAUTHORIZED);
		}
		
		System.out.println("Listing all sessions for email: " + email);
		
		return new ResponseEntity<>(redisService.getAllKeys(email), HttpStatus.OK);
	}

}
