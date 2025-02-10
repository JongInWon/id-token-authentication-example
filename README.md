# ID Token Authentication Example

소셜 로그인(Apple, Kakao)에서 발급받은 ID Token을 검증하는 예제 프로젝트입니다. 전략 패턴을 활용하여 다양한 소셜 로그인 검증 로직을 유연하게 처리합니다.

## 주요 설계: 전략 패턴 (Strategy Pattern)

이 프로젝트는 전략 패턴을 사용하여 다음과 같은 이점을 제공합니다:

- 각 소셜 로그인 제공자(Apple, Kakao)의 ID Token 검증 로직을 캡슐화
- 새로운 소셜 로그인 추가 시 기존 코드 수정 없이 확장 가능
- 런타임에 동적으로 검증 전략 선택 가능

### 전략 패턴 구현

- `OidcProvider` : ID Token 검증을 위한 전략 인터페이스
- `AppleOidcProvider` : Apple 전용 검증 구현체
- `KakaoOidcProvider` : Kakao 전용 검증 구현체
- `OidcProviderFactory`: 전략 객체 생성을 담당하는 팩토리

## 기술 스택

- Java 17
- Spring Boot 3.4.2
- auth0 java-jwt 4.5.0
- auth0 jwks-rsa 0.22.1

## 주요 기능

- Apple ID Token 검증 및 사용자 식별자 추출
- Kakao ID Token 검증 및 사용자 식별자 추출
- JWKS(JSON Web Key Set)를 이용한 공개키 검증
- 공개키 캐싱 (7일)

## 프로젝트 구조

```
src/main/java/com/example/oidc/
├── controller/
│   ├── SocialAuthController.java
│   ├── request/
│   └── OidcLoginRequest.java
├── provider/
│   ├── AppleOidcProvider.java
│   ├── KakaoOidcProvider.java
│   ├── OidcProvider.java
│   └── OidcProviderFactory.java
├── service/
│   └── SocialAuthService.java
├── enums/
└── SocialType.java
```

## 설정

`application.yml`에 소셜 로그인 관련 설정을 추가해야 합니다:

```yaml
social:
  kakao:
    audience: "your-kakao-client-id"
    issuer: "https://kauth.kakao.com"
    jwksUrl: "https://kauth.kakao.com/.well-known/jwks.json"
  apple:
    audience: "your-apple-client-id"
    issuer: "https://appleid.apple.com"
    jwksUrl: "https://appleid.apple.com/auth/keys"
```

## API 엔드포인트

### 소셜 로그인

```
POST /api/v1/login
Content-Type: application/json
{
    "socialType": "APPLE", // 또는 "KAKAO"
    "idToken": "your.id.token"
}
```

## 빌드 및 실행

```bash
프로젝트 빌드
./gradlew build

애플리케이션 실행
./gradlew bootRun
```
