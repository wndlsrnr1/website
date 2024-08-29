package com.website.controller.api.user.model;

import com.website.service.user.model.UserUpdateDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserUpdateRequest {
    private String address;
    private String username;

    public UserUpdateDto toDto(UserUpdateRequest request) {
        return UserUpdateDto.builder()
                .username(request.getUsername())
                .address(request.getAddress())
                .build();
    }
}
