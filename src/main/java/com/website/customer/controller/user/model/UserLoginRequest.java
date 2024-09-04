package com.website.customer.controller.user.model;

import com.website.customer.service.user.model.LoginRequestDto;
import lombok.Data;

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
