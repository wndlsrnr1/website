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
import com.website.service.user.model.*;
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
import java.util.*;

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
    public String login(KaKaoAuthRequestDto dto) {
        log.info("KaKaoAuthRequestDto = {}", dto);
        KakaoAuthResponseDto kakaoAuthResponseDto = getKakaoAuthRequest(dto, LOGIN_REDIRECT_URI);
        KaKaoUserInfoDto kaKaoUserInfoDto = getKaKaoUserInfoDto(kakaoAuthResponseDto);

        Optional<User> findUser = userRepository.findByEmail(kaKaoUserInfoDto.getEmail());
        if (findUser.isPresent()) {
            return jwtUtil.generateToken(findUser.get().getEmail());
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
        return jwtUtil.generateToken(savedUser.getEmail());
    }

    @Transactional
    public void deleteUser(ServiceUser serviceUser, KaKaoAuthRequestDto dto) {
        userValidator.validateUserExists(serviceUser.getId());

        //validate user exists
        User foundUser = userRepository.findByEmailAndSocialType(serviceUser.getEmail(), SocialType.KAKAO).orElseThrow(() ->
                new ClientException(ErrorCode.BAD_REQUEST, "user not exists. request = " + dto));

        //validate user equal to db
        validateUserEqualToRequestUserInfo(serviceUser.getId(), foundUser.getId());

        KakaoAuthResponseDto authResponseDto = getKakaoAuthRequest(dto, DELETE_REDIRECT_URI);
        KaKaoUserInfoDto kaKaoUserInfoDto = getKaKaoUserInfoDto(authResponseDto);

        //validate user equal to kakao user.
        if (!foundUser.getEmail().equals(kaKaoUserInfoDto.getEmail())) {
            throw new UnauthorizedActionException(ErrorCode.UNAUTHORIZED, "client's email not match to src email user = " + serviceUser);
        }

        KakaoUserAudInfoDto audInfo = KakaoUserAudInfoDto.builder()
                .targetIdType("user_id")
                .targetId(Long.parseLong(kaKaoUserInfoDto.getSub()))
                .build();

        Long disconnectedSub = disconnectKakaoAccessRequest(authResponseDto.getAccessToken(), audInfo);
        log.info("disconnectedSub = {}", disconnectedSub);
        userRepository.deleteById(serviceUser.getId());
    }

    public void logout(ServiceUser serviceUser, KaKaoAuthRequestDto kakaoAuthRequest) {
        KakaoAuthResponseDto authResponseDto = getKakaoAuthRequest(kakaoAuthRequest, LOGOUT_REDIRECT_URI);
        KaKaoUserInfoDto kaKaoUserInfoDto = getKaKaoUserInfoDto(authResponseDto);

        if (!serviceUser.getEmail().equals(kaKaoUserInfoDto.getEmail())) {
            throw new UnauthorizedActionException(ErrorCode.UNAUTHORIZED, "client's email not match to src email user = " + serviceUser);
        }

        KakaoUserAudInfoDto kakaoAccessExpireRequestDto = KakaoUserAudInfoDto.builder()
                .targetIdType("user_id")
                .targetId(Long.parseLong(kaKaoUserInfoDto.getSub()))
                .build();

        //여기 완성하기
        Long deletedSub = expireAccessKakaoRequest(authResponseDto.getAccessToken(), kakaoAccessExpireRequestDto);
    }

    //public String login(KaKaoAuthRequestDto dto) {
    //    KakaoAuthResponseDto kakaoAuthResponseDto = getKakaoAuthRequest(dto, LOGIN_REDIRECT_URI);
    //    KaKaoUserInfoDto kaKaoUserInfoDto = getKaKaoUserInfoDto(kakaoAuthResponseDto);
    //
    //    User user = userRepository.findByEmail(kaKaoUserInfoDto.getEmail())
    //            .orElseThrow(() -> new ClientException(ErrorCode.BAD_REQUEST, "credential is not correct. request" + dto));
    //
    //    User foundUser = userRepository.findByIdAndSocialType(user.getId(), SocialType.KAKAO).orElseThrow(() ->
    //            new ClientException(ErrorCode.BAD_REQUEST, "credential is not correct. request" + dto)
    //    );
    //
    //    return jwtUtil.generateToken(foundUser.getEmail());
    //}


    private void validateUserEqualToRequestUserInfo(Long userId, Long dbUserId) {
        if (!userId.equals(dbUserId)) {
            throw new ClientException(ErrorCode.BAD_REQUEST,
                    "user not matches dbUser. userId = " + userId);
        }
    }


    private KaKaoUserInfoDto getKaKaoUserInfoDto(KakaoAuthResponseDto authorizedData) {
        String payLoad = authorizedData.getIdToken().split("[.]")[1];
        String userInfo = new String(Base64.getDecoder().decode(payLoad), StandardCharsets.UTF_8);
        log.info("userInfo = {}", userInfo);
        KaKaoUserInfoDto kaKaoUserInfoDto = null;
        try {
            kaKaoUserInfoDto = objectMapper.readValue(userInfo, KaKaoUserInfoDto.class);
        } catch (JsonProcessingException e) {
            throw new ServerException(ErrorCode.INTERNAL_SERVER_ERROR, "json write value errors. src = " + userInfo, e);
        }
        log.info("kaKaoUserInfoDto = {}", kaKaoUserInfoDto);
        return kaKaoUserInfoDto;
    }

    private KakaoAuthResponseDto getKakaoAuthRequest(KaKaoAuthRequestDto dto, KaKaoLoginConstant method) {

        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        log.info("dto = {}", dto);
        formData.add("code", dto.getCode());
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", CLIENT_ID.getValue());
        formData.add("redirect_uri", method.getValue());

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

    private Long disconnectKakaoAccessRequest(String accessToken, KakaoUserAudInfoDto audInfo) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + accessToken);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("target_id_type", audInfo.getTargetIdType());
        requestBody.add("target_id", audInfo.getTargetId().toString());
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.postForEntity(
                    AUTH_DISCONNECT_URL.getValue(),
                    new HttpEntity<>(requestBody, headers),
                    String.class
            );
        } catch (HttpClientErrorException exception) {
            throw new UnauthorizedActionException(ErrorCode.UNAUTHORIZED, "logout request to kakao fail. request url = " + AUTH_LOGOUT_URL.getValue(), exception);
        }
        if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            throw new UnauthorizedActionException(ErrorCode.UNAUTHORIZED, "kakao auth fail on logout.");
        }

        String before = responseEntity.getBody().split(":")[1];
        return Long.valueOf(before.substring(0, before.length() - 1));
    }

    private Long expireAccessKakaoRequest(String accessToken, KakaoUserAudInfoDto audInfo) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + accessToken);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("target_id_type", audInfo.getTargetIdType());
        requestBody.add("target_id", audInfo.getTargetId().toString());
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.postForEntity(
                    AUTH_LOGOUT_URL.getValue(),
                    new HttpEntity<>(requestBody, headers),
                    String.class
            );
        } catch (HttpClientErrorException exception) {
            throw new UnauthorizedActionException(ErrorCode.UNAUTHORIZED, "disconnect request to kakao fail. request url = " + AUTH_DISCONNECT_URL.getValue(), exception);
        }

        if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            throw new UnauthorizedActionException(ErrorCode.UNAUTHORIZED, "kakao auth fail on logout.");
        }

        String before = responseEntity.getBody().split(":")[1];
        return Long.valueOf(before.substring(0, before.length() - 1));
    }
}
