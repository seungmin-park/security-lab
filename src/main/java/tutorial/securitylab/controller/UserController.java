package tutorial.securitylab.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@Slf4j
@RestController
public class UserController {

    @Value("${spring.security.oauth2.client.registration.github.client-id}")
    private String clientId;

    @GetMapping("/api/auth/github")
    public String git() {
        log.info("{}", clientId);
        return "https://github.com/login?client_id=" + clientId;
    }

    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("name", principal.getAttributes());
    }
}
