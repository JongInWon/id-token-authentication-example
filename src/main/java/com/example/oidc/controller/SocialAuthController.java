package com.example.oidc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.oidc.controller.request.OidcLoginRequest;
import com.example.oidc.service.SocialAuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SocialAuthController {

	private final SocialAuthService socialAuthService;

	@PostMapping("/login")
	public ResponseEntity<String> socialLogin(@RequestBody final OidcLoginRequest request) {

		String tokenResponse = socialAuthService.login(request);
		return ResponseEntity.ok(tokenResponse);
	}

}