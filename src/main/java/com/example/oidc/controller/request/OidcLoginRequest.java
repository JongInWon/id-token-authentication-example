package com.example.oidc.controller.request;

import com.example.oidc.enums.SocialType;

public record OidcLoginRequest(
	SocialType socialType,
	String idToken
) {
}
