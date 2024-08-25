package com.website.config.auth;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {
    private final JwtUtil jwt = new JwtUtil(
            "V1RFTEpCSlRVUkRQVVBHUFBQSE5NTk1BVlNNU0pGV0JCWUpSU0dHVVVKTFRHVk5NUUdKWUQ",
            1000L
    );

    @Test
    @DisplayName("Token 검증 - 256bit secret key 입력시 검증 성공")
    public void testJwt() throws Exception {
        // Given

        String email = "test@naver.com";
        String token = jwt.generateToken(email);

        // When

        boolean validated = jwt.validateToken(token);

        // Then
        assertThat(validated).isTrue();

    }
}