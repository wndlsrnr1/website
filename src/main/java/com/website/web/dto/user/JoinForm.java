package com.website.web.dto.user;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JoinForm {
    private String email;
    private String name;
    private String password;
    private String passWord2;
    private String address;

}
