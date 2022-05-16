package tutorial.securitylab.controller;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@RestController
public class UserController {

    @Value("${spring.security.oauth2.client.registration.github.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.github.client-secret}")
    private String clientSecret;

    @GetMapping("/api/auth/github")
    public String git() {
        log.info("{}", clientId);
        StringBuilder sb = new StringBuilder();
        sb.append("https://github.com/login/oauth/authorize?client_id=");
        sb.append(clientId);
        sb.append("&scope=user");
        sb.append("&redirect_uri=http://localhost:8080/test");
        return sb.toString();
    }
    }

    @GetMapping("/test")
    public String ttttt(@RequestParam String code){
        log.info("method start");
        log.info("code={}", code);

        String accessUrl = "https://github.com/login/oauth/access_token";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("client_id", clientId);
        jsonObject.put("client_secret", clientSecret);
        jsonObject.put("code", code);

        HttpEntity<String> entity = new HttpEntity<>(jsonObject.toString(), httpHeaders);
        String temp = restTemplate.postForObject(accessUrl, entity, String.class);
        String access_token = temp.split("=")[1].split("&")[0];
        log.info("access_token={}", access_token);

        httpHeaders.clear();

        httpHeaders.add("Authorization","token "+ access_token);
        HttpEntity<Object> http = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange("https://api.github.com/user", HttpMethod.GET, http, String.class);
        String body = response.getBody();

        log.info("body={}",body);
        return body;
    }
}
