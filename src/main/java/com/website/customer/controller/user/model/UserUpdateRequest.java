package com.website.customer.controller.user.model;

import com.website.customer.service.user.model.UserUpdateDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
