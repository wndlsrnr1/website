package com.website.controller.api.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.website.config.auth.LoginUser;
import com.website.config.auth.ServiceUser;
import com.website.controller.api.common.model.ApiResponse;
import com.website.controller.api.user.model.*;
import com.website.repository.user.model.UserRole;
import com.website.service.user.UserKakaoService;
import com.website.service.user.UserService;
import com.website.service.user.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Validated
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final UserKakaoService userKakaoService;
    private final ObjectMapper objectMapper;

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

    @LoginUser
    @PatchMapping("/users/me")
    public ApiResponse<UserResponse> updateUser(
            @AuthenticationPrincipal ServiceUser serviceUser,
            @Valid @RequestBody UserUpdateRequest request
    ) {
        log.info("request = {}", request);
        UserUpdateDto userUpdateDto = request.toDto(request);
        UserDto userDto = userService.updateUser(serviceUser, userUpdateDto);
        UserResponse userResponse = UserResponse.of(userDto);
        return ApiResponse.success(userResponse);
    }

    @PostMapping("/users/check/email")
    public ApiResponse<EmailCheckResponse> loginCheck(
            @Valid @RequestBody EmailCheckRequest request
    ) {
        EmailCheckResponseDto dto = userService.validateEmail(request.toDto());
        EmailCheckResponse body = EmailCheckResponse.of(dto);
        return ApiResponse.success(body);
    }

    @GetMapping("/auth/users")
    public ApiResponse<Void> adminCheck(
            @AuthenticationPrincipal ServiceUser serviceUser
    ) {
        serviceUser.getAuthorities().stream().filter(authority -> authority.getAuthority().equals(UserRole.ADMIN.name())).findAny().orElseThrow(
                () -> new AccessDeniedException("Access Denied. user = " + serviceUser)
        );
        return ApiResponse.success();
    }

    //https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=b678b03b04bcdee81052dad6e436c06c&redirect_uri=http://localhost:8080/auth/users/kakao
    //@PostMapping("/auth/users/kakao")

    @PostMapping("/auth/login/kakao")
    public ApiResponse<String> login(
            @RequestBody @Valid KaKaoAuthRequest kaKaoAuthRequest
    ) {
        KaKaoAuthRequestDto dto = kaKaoAuthRequest.toDto();
        String token = userKakaoService.login(dto);
        return ApiResponse.success(token);
    }

    @LoginUser
    @PostMapping("/auth/logout/kakao")
    public ApiResponse<Void> logout(
            @AuthenticationPrincipal ServiceUser serviceUser,
            @RequestBody @Valid KaKaoAuthRequest kaKaoAuthRequest
    ) {
        KaKaoAuthRequestDto dto = kaKaoAuthRequest.toDto();
        userKakaoService.logout(serviceUser, dto);
        return ApiResponse.success();
    }

    @LoginUser
    @DeleteMapping("/auth/users/kakao")
    public ApiResponse<Void> delete(
            @AuthenticationPrincipal ServiceUser serviceUser,
            @RequestBody @Valid KaKaoAuthRequest kaKaoAuthRequest
    ) {
        KaKaoAuthRequestDto authRequestDto = kaKaoAuthRequest.toDto();
        userKakaoService.deleteUser(serviceUser, authRequestDto);
        return ApiResponse.success();
    }

    @LoginUser
    @GetMapping("/users")
    public ApiResponse<UserResponse> getUser (
            @AuthenticationPrincipal ServiceUser serviceUser
    ) {
        UserDto userDto = userService.getUser(serviceUser.getId());
        UserResponse userResponse = UserResponse.of(userDto);
        return ApiResponse.success(userResponse);
    }

    @LoginUser
    @PostMapping("/users/me/password")
    public ApiResponse<Void> updatePassword(
            @AuthenticationPrincipal ServiceUser serviceUser,
            @RequestBody @Valid UpdatePasswordRequest request
    ) {
        UpdatePasswordRequestDto dto = request.toDto();
        userService.updatePassword(serviceUser, dto);
        return ApiResponse.success();
    }
}
