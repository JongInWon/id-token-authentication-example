package com.example.oidc.provider;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwk.InvalidPublicKeyException;
import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class KakaoOidcProvider implements OidcProvider {

	private final JwkProvider kakaoJwkProvider;
	private final String issuer;
	private final String audience;

	public KakaoOidcProvider(
		@Value("${social.kakao.jwksUrl}") final String jwksUrl,
		@Value("${social.kakao.issuer}") final String issuer,
		@Value("${social.kakao.audience}") final String audience
	) {
		this.issuer = issuer;
		this.audience = audience;

		try {
			this.kakaoJwkProvider = new JwkProviderBuilder(new URL(jwksUrl))
				.cached(10, 7, TimeUnit.DAYS) // 최대 10개 키를 7일간 캐싱
				.build();
		} catch (MalformedURLException e) {
			throw new RuntimeException("유효하지 않은 Kakao JWKS URL입니다.", e);
		}
	}

	@Override
	public String extractUserId(final String idToken) {

		DecodedJWT verifiedToken = verifyToken(idToken);

		return verifiedToken.getSubject();
	}

	private DecodedJWT verifyToken(final String idToken) {

		DecodedJWT decoded = decodeToken(idToken);
		Jwk jwk = fetchJwk(decoded.getKeyId());
		RSAPublicKey publicKey = extractPublicKey(jwk);
		JWTVerifier verifier = createVerifier(publicKey);

		return verifier.verify(idToken);
	}

	private DecodedJWT decodeToken(final String idToken) {

		try {
			return JWT.decode(idToken);
		} catch (Exception e) {
			throw new RuntimeException("Kakao id token 디코딩에 실패했습니다.", e);
		}
	}

	private Jwk fetchJwk(final String kid) {

		try {
			return kakaoJwkProvider.get(kid);
		} catch (JwkException e) {
			throw new RuntimeException("Kakao JWKS 처리에 실패했습니다.", e);
		}
	}

	private RSAPublicKey extractPublicKey(final Jwk jwk) {

		try {
			return (RSAPublicKey)jwk.getPublicKey();
		} catch (InvalidPublicKeyException e) {
			throw new RuntimeException("유효하지 않은 공개키입니다.", e);
		}
	}

	private JWTVerifier createVerifier(final RSAPublicKey publicKey) {

		return JWT.require(Algorithm.RSA256(publicKey, null))
			.withIssuer(issuer)
			.withAudience(audience)
			.build();
	}

}