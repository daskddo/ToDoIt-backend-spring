package ToDoIt.backend.service;

import ToDoIt.backend.DTO.KakaoTokenResponseDto;
import ToDoIt.backend.DTO.KakaoUserInfoResponseDto;
import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@Service
public class KakaoService {
    private final String clientId;
    private final String clientSecret;
    private final String KAUTH_TOKEN_URL_HOST ;
    private final String KAUTH_USER_URL_HOST;

    @Autowired
    public KakaoService(@Value("${kakao.client-id}") String clientId, @Value("${kakao.client-secret}") String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        KAUTH_TOKEN_URL_HOST ="https://kauth.kakao.com";
        KAUTH_USER_URL_HOST = "https://kapi.kakao.com";
    }

    // 프론트없이 테스트하기 위한 코드(추후 삭제)
    public String getAccessTokenFromKakao(String code) {
        try {
            KakaoTokenResponseDto kakaoTokenResponseDto = WebClient.create(KAUTH_TOKEN_URL_HOST).post()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("https")
                            .path("/oauth/token")
                            .queryParam("grant_type", "authorization_code")
                            .queryParam("client_id", clientId)
                            .queryParam("client_secret", clientSecret)
                            .queryParam("code", code)
                            .build(true))
                    .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                    .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                    .bodyToMono(KakaoTokenResponseDto.class)
                    .block();

            log.info(" [Kakao Service] Access Token ------> {}", kakaoTokenResponseDto.getAccessToken());
            log.info(" [Kakao Service] Refresh Token ------> {}", kakaoTokenResponseDto.getRefreshToken());
            log.info(" [Kakao Service] Id Token ------> {}", kakaoTokenResponseDto.getIdToken());
            log.info(" [Kakao Service] Scope ------> {}", kakaoTokenResponseDto.getScope());

            return kakaoTokenResponseDto.getAccessToken();
        } catch (WebClientResponseException.Unauthorized ex) {
            // Log the specific error response from Kakao
            log.error(" [Kakao Service] Error obtaining access token: {}", ex.getResponseBodyAsString());
            throw new RuntimeException("Unauthorized: Invalid credentials or token request");
        } catch (Exception ex) {
            log.error(" [Kakao Service] Error obtaining access token", ex);
            throw new RuntimeException("Failed to obtain access token from Kakao", ex);
        }
    }

    // access token으로 사용자 정보 가져오기
    public KakaoUserInfoResponseDto getUserInfo(String accessToken) {
        KakaoUserInfoResponseDto userInfo = WebClient.create(KAUTH_USER_URL_HOST)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/v2/user/me")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // access token 인가
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                //TODO : Custom Exception
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(KakaoUserInfoResponseDto.class)
                .block();

        log.info("[ Kakao Service ] Auth ID ---> {} ", Objects.requireNonNull(userInfo).getId());
        log.info("[ Kakao Service ] NickName ---> {} ", userInfo.getKakaoAccount().getProfile().getNickName());
        log.info("[ Kakao Service ] ProfileImageUrl ---> {} ", userInfo.getKakaoAccount().getProfile().getProfileImageUrl());
        log.info("[ Kakao Service ] Email ---> {} ", userInfo.getKakaoAccount().getEmail());

        return userInfo;
    }
}