package com.website.controller.api.user.model;

import com.website.utils.common.constance.Regexes;
import com.website.service.user.model.UserDeleteDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDeleteRequest {

    @NotBlank
    @Email(regexp = Regexes.EMAIL_PATTERN)
    private String email;

    @NotBlank
    @Pattern(regexp = Regexes.PASSWORD_PATTERN)
    private String password;

    public UserDeleteDto toDto(UserDeleteRequest request) {
        return UserDeleteDto.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }
}
