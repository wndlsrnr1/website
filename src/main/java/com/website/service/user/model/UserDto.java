package com.website.service.user.model;

import com.website.repository.user.model.SocialType;
import com.website.repository.user.model.User;
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
    private String email;
    private String address;
    private String password;
    private SocialType socialType;

    public static UserDto of(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .address(user.getAddress())
                .socialType(user.getSocialType())
                .password(user.getPassword())
                .build();
    }

}
