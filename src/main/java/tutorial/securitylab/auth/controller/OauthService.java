package tutorial.securitylab.auth.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tutorial.securitylab.auth.dto.MemberResponse;
import tutorial.securitylab.auth.dto.OauthAccessTokenRequest;
import tutorial.securitylab.auth.dto.OauthAccessTokenResponse;
import tutorial.securitylab.auth.dto.SignInRequest;

@Service
public class OauthService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token";
    private final String USER_INFO_URL = "https://api.github.com/user";
    @Value("${security.oauth.github.client-id}")
    private String clientId;
    @Value("${security.oauth.github.client-secret}")
    private String clientSecret;

    public MemberResponse getUserInfo(SignInRequest request) {
        String accessToken = getAccessToken(request);
        return userInfo(accessToken);
    }

    private String getAccessToken(SignInRequest request) {
        return restTemplate.postForObject(
            ACCESS_TOKEN_URL,
            new OauthAccessTokenRequest(clientId, clientSecret, request.getCode()),
            OauthAccessTokenResponse.class
        ).getAccessToken();
    }

    private MemberResponse userInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Object> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(USER_INFO_URL, HttpMethod.GET, entity, MemberResponse.class).getBody();
    }
}
