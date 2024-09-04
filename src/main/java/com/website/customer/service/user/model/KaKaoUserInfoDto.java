package com.website.customer.service.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KaKaoUserInfoDto {
    private String aud;
    private String sub;
    private Long auth_time;
    private String iss;
    private Long exp;
    private Long iat;
    private String email;
}
