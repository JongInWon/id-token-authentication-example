package com.example.oidc.service;

import org.springframework.stereotype.Service;

import com.example.oidc.controller.request.OidcLoginRequest;
import com.example.oidc.provider.OidcProviderFactory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SocialAuthService {

	private final OidcProviderFactory oidcProviderFactory;

	public String login(final OidcLoginRequest request) {

		String userIdentifier = oidcProviderFactory.extractUserId(request.socialType(), request.idToken());

		// 추가 로직

		return userIdentifier;
	}
}