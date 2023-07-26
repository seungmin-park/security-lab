package tutorial.securitylab.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MemberResponse {
    @JsonProperty("login")
    private String name;
    @JsonProperty("avatar_url")
    private String profileUrl;
}
