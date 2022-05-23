package tutorial.securitylab.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import tutorial.securitylab.config.JwtTokenProvider;
import tutorial.securitylab.domain.Member;
import tutorial.securitylab.repository.MemberRepository;

import java.util.Collections;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    private final PasswordEncoder passwordEncoder;

    @Value("${spring.security.oauth2.client.registration.github.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.github.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.github.redirect-uri}")
    private String redirectUrl;

    @GetMapping("/api/auth/github")
    public String git() {
        log.info("{}", clientId);
        StringBuilder sb = new StringBuilder();
        sb.append("https://github.com/login/oauth/authorize?client_id=");
        sb.append(clientId);
        sb.append("&scope=user");
        sb.append("&redirect_uri=");
        sb.append(redirectUrl);
        return sb.toString();
    }

    @GetMapping("/api/auth/callback/github")
    public String testMethod(@RequestParam String code) throws ParseException {
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

        httpHeaders.add("Authorization", "token " + access_token);
        HttpEntity<Object> http = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange("https://api.github.com/user", HttpMethod.GET, http, String.class);
        JSONParser jsonParser = new JSONParser();
        Object parse = jsonParser.parse(response.getBody());
        log.info(response.getBody());
        JSONObject object = (JSONObject) parse;

        String login = (String) object.get("login");
        String password = (String) object.get("node_id");
        Member member = Member
                .builder()
                .memberName(login)
                .username(login)
                .password(passwordEncoder.encode(password))
                .roles(Collections.singletonList("ROLE_USER"))
                .build();

        memberRepository.save(member);

        String token = jwtTokenProvider.generateToken(String.valueOf(member.getMemberId()), member.getRoles());
        return token;
    }

    @GetMapping("/success")
    public String ok() {
        return "ok";
    }
}
