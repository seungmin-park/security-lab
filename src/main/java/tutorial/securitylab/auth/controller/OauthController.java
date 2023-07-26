package tutorial.securitylab.auth.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import tutorial.securitylab.auth.controller.OauthAccessTokenRequest;

@RestController
public class OauthController {

    private final String ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token";
    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${security.oauth.github.client-id}")
    private String clientId;
    @Value("${security.oauth.github.client-secret}")
    private String clientSecret;

    @GetMapping("/callback")
    public String callback(@RequestParam String code) {
        return restTemplate.postForObject(
            ACCESS_TOKEN_URL,
            new OauthAccessTokenRequest(clientId, clientSecret, code),
            OauthAccessTokenResponse.class
        ).getAccessToken();
    }
}
