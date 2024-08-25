package com.website.controller.api.user.model;

import com.website.service.user.model.LoginRequestDto;
import com.website.utils.common.constance.Regexes;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UserLoginRequest {
    @NotBlank
    @Email(regexp = Regexes.EMAIL_PATTERN)
    private String email;

    @NotBlank
    @Pattern(regexp = Regexes.PASSWORD_PATTERN)
    private String password;

    public LoginRequestDto toDto() {
        return LoginRequestDto.builder()
                .email(email)
                .password(password)
                .build();
    }
}
