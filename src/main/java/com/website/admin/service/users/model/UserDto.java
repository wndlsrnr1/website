package com.website.admin.service.users.model;


import com.website.common.repository.user.model.SocialType;
import com.website.common.repository.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long userId;
    private String email;
    private String address;
    private SocialType socialType;
    private LocalDateTime createdAt;
    private String username;

    public static UserDto of(User user) {
        return UserDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .address(user.getAddress())
                .socialType(user.getSocialType())
                .createdAt(user.getCreatedAt())
                .username(user.getName())
                .build();
    }
}
