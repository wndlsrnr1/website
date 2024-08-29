package com.website.service.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.website.config.auth.JwtUtil;
import com.website.config.auth.ServiceUser;
import com.website.exception.ClientException;
import com.website.exception.ErrorCode;
import com.website.exception.UnauthorizedActionException;
import com.website.repository.user.model.SocialType;
import com.website.repository.user.model.User;
import com.website.repository.user.UserRepository;
import com.website.repository.user.model.UserRole;
import com.website.service.user.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.*;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,}$");
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder;
    private final UserValidator userValidator;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public String login(LoginRequestDto dto) {
        validatePassword(dto.getPassword());
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ClientException(ErrorCode.BAD_REQUEST, "credential is not correct. request" + dto));

        User foundUser = userRepository.findByIdAndSocialType(user.getId(), SocialType.NONE).orElseThrow(() ->
                new ClientException(ErrorCode.BAD_REQUEST, "credential is not correct. request" + dto)
        );

        if (!passwordEqual(dto.getPassword(), foundUser.getPassword())) {
            throw new ClientException(ErrorCode.BAD_REQUEST, "credential is not correct. request=" + dto);
        }

        return jwtUtil.generateToken(dto.getEmail());
    }

    @Transactional(readOnly = true)
    public UserDto getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ClientException(ErrorCode.BAD_REQUEST, "cannot found user. userId=" + userId)
                );

        return UserDto.of(user);
    }

    @Transactional
    public UserDto register(UserRegisterRequestDto dto) {
        validatePassword(dto.getPassword());
        Optional<User> findUser = userRepository.findByEmail(dto.getEmail());
        if (findUser.isPresent()) {
            throw new ClientException(ErrorCode.BAD_REQUEST, "user's email already exists. " + dto.getEmail());
        }

        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .address(dto.getAddress())
                .socialType(SocialType.NONE)
                .roles(List.of(UserRole.USER))
                .build();

        User savedUser = userRepository.save(user);
        return UserDto.of(savedUser);
    }


    @Transactional
    public UserDto updateUser(ServiceUser serviceUser, UserUpdateDto userUpdateDto) {
        User user = userValidator.validateAndGet(serviceUser.getId());
        if (userUpdateDto.getAddress() != null) {
            user.setAddress(userUpdateDto.getAddress());
        }
        if (userUpdateDto.getUsername() != null) {
            user.setName(userUpdateDto.getUsername());
        }
        User savedUser = userRepository.save(user);
        return UserDto.of(savedUser);
    }

    @Transactional
    public void deleteUser(ServiceUser serviceUser, UserDeleteDto dto) {
        userValidator.validateUserExists(serviceUser.getId());

        validatePassword(dto.getPassword());

        User foundUser = userRepository.findByEmailAndSocialType(dto.getEmail(), SocialType.NONE).orElseThrow(() ->
                new ClientException(ErrorCode.BAD_REQUEST, "user not exists. request = " + dto));

        validateUserEqualToRequestUserInfo(serviceUser.getId(), foundUser.getId());

        userRepository.deleteById(foundUser.getId());
    }

    @Transactional
    public void updatePassword(ServiceUser serviceUser, UpdatePasswordRequestDto dto) {
        User foundUser = userValidator.validateAndGet(serviceUser.getId());
        if (!foundUser.getSocialType().equals(SocialType.NONE)) {
            throw new UnauthorizedActionException(ErrorCode.BAD_REQUEST,
                    "social user can not change password. userId = " + serviceUser.getId());
        }

        if (!passwordEqual(dto.getCurrentPassword(), serviceUser.getPassword())) {
            throw new UnauthorizedActionException(ErrorCode.UNAUTHORIZED,
                    "requested password not equal to user's password. userId = " + serviceUser.getId());
        }

        userValidator.validatePasswordConfirmation(serviceUser.getId(), dto.getNewPassword(), dto.getConfirmPassword());

    }


    private boolean passwordEqual(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }

    private void validateUserEqualToRequestUserInfo(Long userId, Long dbUserId) {
        if (!userId.equals(dbUserId)) {
            throw new ClientException(ErrorCode.BAD_REQUEST,
                    "user not matches dbUser. userId = " + userId);
        }
    }

    private void validatePassword(String password) {
        if (password == null || !PASSWORD_PATTERN.matcher(password).matches()) {
            throw new ClientException(ErrorCode.BAD_REQUEST, "invalid password. password=" + password);
        }
    }


    public EmailCheckResponseDto validateEmail(EmailCheckRequestDto email) {
        //User가 있으면 문제 있음
        //if (userRepository.findByEmail(email.getEmail()).isPresent()) {
        //    throw new ClientException(ErrorCode.BAD_REQUEST,
        //            "email already exists. requested email = " + email);
        //}
        if (userRepository.findByEmail(email.getEmail()).isPresent()) {
            return EmailCheckResponseDto.builder().emailExists(false).build();
        }
        return EmailCheckResponseDto.builder().emailExists(true).build();
    }
}