package com.website.controller.api.user.model;

import com.website.repository.user.model.SocialType;
import com.website.service.user.model.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String email;
    private String address;
    private SocialType socialType;
    private LocalDateTime createdAt;
    private String username;

    public static UserResponse of(UserDto user) {
        return UserResponse.builder()
                .email(user.getEmail())
                .address(user.getAddress())
                .socialType(user.getSocialType())
                .createdAt(user.getCreatedAt())
                .username(user.getUsername())
                .build();
    }

}
