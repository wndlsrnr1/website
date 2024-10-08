package com.website.customer.service.user.model;

import com.website.common.repository.user.model.SocialType;
import com.website.common.repository.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private String address;
    private String password;
    private SocialType socialType;
    private LocalDateTime createdAt;
    private String username;

    public static UserDto of(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .address(user.getAddress())
                .socialType(user.getSocialType())
                .password(user.getPassword())
                .createdAt(user.getCreatedAt())
                .username(user.getName())
                .build();
    }

}
