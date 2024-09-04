package com.website.admin.controller.users;

import com.website.config.auth.AdminUser;
import com.website.admin.controller.users.model.UserResponse;
import com.website.common.controller.model.ApiResponse;
import com.website.admin.service.users.AdminUsersService;
import com.website.admin.service.users.model.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@Slf4j
@RequiredArgsConstructor
public class AdminUsersController {

    private final AdminUsersService adminUsersService;

    @AdminUser
    @GetMapping("/users/{userId}")
    public ApiResponse<UserResponse> getUser(
            @PathVariable(value = "userId") Long userId
    ) {
        UserDto dto = adminUsersService.getUser(userId);

        return ApiResponse.success(UserResponse.of(dto));
    }
}
