package com.website.controller.api.user.model;

import com.website.service.user.model.UserRegisterRequestDto;
import com.website.utils.common.constance.Regexes;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class UserRegisterRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = Regexes.PASSWORD_PATTERN)
    private String password;

    @Length(min = 3, max = 20)
    @NotBlank
    @NotNull
    private String name;

    @NotBlank
    private String address;

    public UserRegisterRequestDto toDto() {
        return UserRegisterRequestDto.builder()
                .email(email)
                .name(name)
                .password(password)
                .address(address)
                .build();
    }
}

