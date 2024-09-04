package com.website.customer.service.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IdToken {
    private String aud;
    private String sub;
    @JsonProperty("auth_time")
    private Long authTime;
    private String iss;
    private Long exp;
    private Long iat;
    private String nickname;
    private String picture;
    private String email;
}
