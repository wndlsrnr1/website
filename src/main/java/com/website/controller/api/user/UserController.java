package com.website.controller.api.user;

import com.website.config.auth.LoginUser;
import com.website.config.auth.ServiceUser;
import com.website.controller.api.common.model.ApiResponse;
import com.website.controller.api.user.model.UserDeleteRequest;
import com.website.controller.api.user.model.UserLoginRequest;
import com.website.controller.api.user.model.UserRegisterRequest;
import com.website.controller.api.user.model.UserResponse;
import com.website.service.user.UserService;
import com.website.service.user.model.UserDeleteDto;
import com.website.service.user.model.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Validated
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/auth/login")
    public ApiResponse<String> login(@Valid @RequestBody UserLoginRequest request) {
        //Controller는 request만 매개변수로 받음.
        //Service는 Dto만 매개변수로 받음.
        String token = userService.login(request.toDto());
        return ApiResponse.success(token);
    }

    @PostMapping("/auth/register")
    public ApiResponse<UserResponse> register(@Valid @RequestBody UserRegisterRequest request) {
        UserDto user = userService.register(request.toDto());
        UserResponse userResponse = UserResponse.of(user);
        return ApiResponse.success(userResponse);
    }

    @LoginUser
    @GetMapping("/users/{id}")
    public ApiResponse<UserResponse> getUser(@PathVariable(value = "id") Long id) {
        UserDto user = userService.getUser(id);
        UserResponse userResponse = UserResponse.of(user);
        return ApiResponse.success(userResponse);
    }

    @LoginUser
    @DeleteMapping("/users")
    public ApiResponse<Void> deleteUser(
            @AuthenticationPrincipal ServiceUser serviceUser,
            @Valid @RequestBody UserDeleteRequest request
    ) {
        UserDeleteDto dto = request.toDto(request);
        userService.deleteUser(serviceUser, dto);
        return ApiResponse.success();
    }

}
