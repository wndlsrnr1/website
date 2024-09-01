package com.website.controller.admin.users.model;

import com.website.repository.user.model.SocialType;
import com.website.service.admin.users.model.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Long userId;
    private String email;
    private String address;
    private SocialType socialType;
    private LocalDateTime createdAt;
    private String username;

    public static UserResponse of(UserDto user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .address(user.getAddress())
                .socialType(user.getSocialType())
                .createdAt(user.getCreatedAt())
                .username(user.getUsername())
                .build();
    }

}
