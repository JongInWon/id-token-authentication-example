package com.example.oidc.provider;

import java.util.EnumMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.oidc.enums.SocialType;

@Component
public class OidcProviderFactory {

	private final Map<SocialType, OidcProvider> authProviderMap;
	private final AppleOidcProvider appleOidcProvider;
	private final KakaoOidcProvider kakaoOidcProvider;

	public OidcProviderFactory(
		final AppleOidcProvider appleIdTokenHandler,
		final KakaoOidcProvider kakaoIdTokenHandler
	) {
		this.authProviderMap = new EnumMap<>(SocialType.class);
		this.appleOidcProvider = appleIdTokenHandler;
		this.kakaoOidcProvider = kakaoIdTokenHandler;

		initialize();
	}

	private void initialize() {
		authProviderMap.put(SocialType.APPLE, appleOidcProvider);
		authProviderMap.put(SocialType.KAKAO, kakaoOidcProvider);
	}

	public String extractUserId(final SocialType socialType, final String idToken) {
		return getIdTokenHandler(socialType).extractUserId(idToken);
	}

	private OidcProvider getIdTokenHandler(final SocialType socialType) {
		final OidcProvider oidcProvider = authProviderMap.get(socialType);

		if (oidcProvider == null) {
			throw new RuntimeException("지원하지 않는 소셜 타입입니다.");
		}

		return oidcProvider;
	}

}