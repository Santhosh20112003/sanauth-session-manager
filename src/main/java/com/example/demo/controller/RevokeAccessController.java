package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.NotificationService;

@RestController
@RequestMapping("/revoke-access")
public class RevokeAccessController {

	@Autowired
	private NotificationService notificationService;
	
	@GetMapping("/{email}/{sessionId}")
	public String revokeAccess(@PathVariable String sessionId, @PathVariable String email) {
		
		notificationService.sendRevokeMessage(email,sessionId);
		return "Access revoked for session: " + sessionId;
	}
	
}
