package com.website.controller.api.user.model;

import com.website.service.user.model.LoginRequestDto;
import com.website.utils.common.constance.Regexes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserLoginRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    public LoginRequestDto toDto() {
        return LoginRequestDto.builder()
                .email(email)
                .password(password)
                .build();
    }
}
