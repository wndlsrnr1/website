package com.website.customer.controller.user.model;

import com.website.customer.service.user.model.UserDeleteDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDeleteRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    public UserDeleteDto toDto(UserDeleteRequest request) {
        return UserDeleteDto.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }
}
