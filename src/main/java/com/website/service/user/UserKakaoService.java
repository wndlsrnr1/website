package com.website.service.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.website.config.auth.JwtUtil;
import com.website.config.auth.ServiceUser;
import com.website.exception.ClientException;
import com.website.exception.ErrorCode;
import com.website.exception.ServerException;
import com.website.exception.UnauthorizedActionException;
import com.website.repository.user.UserRepository;
import com.website.repository.user.model.SocialType;
import com.website.repository.user.model.User;
import com.website.repository.user.model.UserRole;
import com.website.service.user.model.KaKaoAuthRequestDto;
import com.website.service.user.model.KaKaoUserInfoDto;
import com.website.service.user.model.KakaoAuthResponseDto;
import com.website.service.user.model.UserDto;
import com.website.utils.common.constance.KaKaoLoginConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import static com.website.utils.common.constance.KaKaoLoginConstant.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserKakaoService {

    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder;
    private final UserValidator userValidator;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    @Transactional(rollbackFor = JsonProcessingException.class)
    public UserDto register(KaKaoAuthRequestDto dto) {
        log.info("KaKaoAuthRequestDto = {}", dto);
        KaKaoUserInfoDto kaKaoUserInfoDto = getKaKaoUserInfoDto(dto);

        Optional<User> findUser = userRepository.findByEmail(kaKaoUserInfoDto.getEmail());
        if (findUser.isPresent()) {
            throw new ClientException(ErrorCode.BAD_REQUEST, "user's email already exists. " + kaKaoUserInfoDto.getEmail());
        }

        User user = User.builder()
                .name(kaKaoUserInfoDto.getEmail().split("@")[0])
                .email(kaKaoUserInfoDto.getEmail())
                .password(encoder.encode(UUID.randomUUID().toString()))
                .address(null)
                .socialType(SocialType.KAKAO)
                .socialUnique(kaKaoUserInfoDto.getSub())
                .roles(List.of(UserRole.USER))
                .build();

        User savedUser = userRepository.save(user);
        return UserDto.of(savedUser);
    }

    @Transactional
    public void deleteUser(ServiceUser serviceUser, KaKaoAuthRequestDto dto) {
        KaKaoUserInfoDto kaKaoUserInfoDto = getKaKaoUserInfoDto(dto);
        userValidator.validateUserExists(serviceUser.getId());
        User foundUser = userRepository.findByEmailAndSocialType(kaKaoUserInfoDto.getEmail(), SocialType.KAKAO).orElseThrow(() ->
                new ClientException(ErrorCode.BAD_REQUEST, "user not exists. request = " + dto));

        validateUserEqualToRequestUserInfo(serviceUser.getId(), foundUser.getId());
        userRepository.deleteById(foundUser.getId());
    }

    public String login(KaKaoAuthRequestDto dto) {
        KaKaoUserInfoDto kaKaoUserInfoDto = getKaKaoUserInfoDto(dto);
        User user = userRepository.findByEmail(kaKaoUserInfoDto.getEmail())
                .orElseThrow(() -> new ClientException(ErrorCode.BAD_REQUEST, "credential is not correct. request" + dto));

        User foundUser = userRepository.findByIdAndSocialType(user.getId(), SocialType.KAKAO).orElseThrow(() ->
                new ClientException(ErrorCode.BAD_REQUEST, "credential is not correct. request" + dto)
        );

        return jwtUtil.generateToken(foundUser.getEmail());
    }

    private void validateUserEqualToRequestUserInfo(Long userId, Long dbUserId) {
        if (!userId.equals(dbUserId)) {
            throw new ClientException(ErrorCode.BAD_REQUEST,
                    "user not matches dbUser. userId = " + userId);
        }
    }

    private KaKaoUserInfoDto getKaKaoUserInfoDto(KaKaoAuthRequestDto dto) {
        KakaoAuthResponseDto authorizedData = getKakaoAuthResponseDto(dto);
        String payLoad = authorizedData.getIdToken().split("[.]")[1];
        String userInfo = new String(Base64.getDecoder().decode(payLoad), StandardCharsets.UTF_8);
        log.info("userInfo = {}" ,userInfo);
        KaKaoUserInfoDto kaKaoUserInfoDto = null;
        try {
            kaKaoUserInfoDto = objectMapper.readValue(userInfo, KaKaoUserInfoDto.class);
        } catch (JsonProcessingException e) {
            throw new ServerException(ErrorCode.INTERNAL_SERVER_ERROR, "json write value errors. src = " + userInfo, e);
        }
        log.info("kaKaoUserInfoDto = {}", kaKaoUserInfoDto);
        return kaKaoUserInfoDto;
    }

    private KakaoAuthResponseDto getKakaoAuthResponseDto(KaKaoAuthRequestDto dto) {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        log.info("dto = {}", dto);
        formData.add("code", dto.getCode());
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", CLIENT_ID.getValue());
        formData.add("redirect_uri", REGISTER_REDIRECT_URI.getValue());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        ResponseEntity<KakaoAuthResponseDto> responseEntity;
        try {
            responseEntity = restTemplate.postForEntity(
                    KaKaoLoginConstant.AUTH_URL.getValue(),
                    new HttpEntity<>(formData, headers),
                    KakaoAuthResponseDto.class
            );
        } catch (HttpClientErrorException exception) {
            throw new UnauthorizedActionException(ErrorCode.UNAUTHORIZED, "authorization code not found for code", exception);
        }

        HttpStatus statusCode = responseEntity.getStatusCode();
        if (!statusCode.equals(HttpStatus.OK)) {
            throw new UnauthorizedActionException(ErrorCode.UNAUTHORIZED, "authorization code not found for code");
        }
        return responseEntity.getBody();
    }

}
