package tutorial.securitylab.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tutorial.securitylab.auth.dto.MemberResponse;
import tutorial.securitylab.auth.dto.SignInRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OauthController {

    private final OauthService oauthService;

    @PostMapping("/api/callback")
    public MemberResponse callback(@Validated @RequestBody SignInRequest request) {
        return oauthService.getUserInfo(request);
    }
}
