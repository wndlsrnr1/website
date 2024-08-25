package com.website.service.user.model;

import com.website.repository.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String password;
    private String email;
    private String address;

    public static UserDto of(User user) {
        return UserDto.builder()
                .id(user.getId())
                .password(user.getPassword())
                .email(user.getEmail())
                .address(user.getAddress())
                .build();
    }
}
