package com.website.utils.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.website.service.user.model.KaKaoUserInfoDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

public class Base64Test {

    @Test
    @DisplayName("[split regex test]")
    void parseTest() {
        // G
        String s = "asdf.asdfas.ss.sdfasdf";
        // W
        String[] split = s.split("[.]");

        // T
        System.out.println("Arrays.toString(split) = " + Arrays.toString(split));
    }

    @Test
    @DisplayName("[jwt to userInfo test]")
    void parseTest2() throws Exception{

        // G
        ObjectMapper objectMapper = new ObjectMapper();
        String token = "MmRhZGQ1ZjIzM2Y5M2QyZmE1MjhkMTJmZWEiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9" +
                ".eyJhdWQiOiJiNjc4YjAzYjA0YmNkZWU4MTA1MmRhZDZlNDM2YzA2YyIsInN1YiI6IjI2ODQ2Mjg1MzQiLCJhdXRoX3RpbWUiOjE3MjQ3NDAxOTQsImlzcyI6Imh0dHBzOi8va2F1dGgua2FrYW8uY29tIiwiZXhwIjoxNzI0NzYxNzk0LCJpYXQiOjE3MjQ3NDAxOTQsImVtYWlsIjoid25kbHNybnIxQG5hdmVyLmNvbSJ9" +
                ".PIHWljIiF4ezcNS_cmGhY84luwqQEUieFpVm8e6giEuRQ7b1kSa1R3VvGT3JFB78zusflOndTtnpPzW6ATA48Ac4-R4ChFiVrtsAAJpBFsu_6x0vKN_H6GP4C0Qlfnw0qXMXI0GkvgOOyxoi0ndKXycu1MKorNnXVFiaolND9oCuUidYy9jlVCwyK2P9lDNtXLwV2U-x35-u0J5KFRFFRBTd5PeI_BaYsPpcEkS1X5RZ9nnJtktwKNCr0Qu55PRuD8wLwMrUHb7svRd9bPu9GCPh5IR5lBhH9SakJXEHkzL0_67Plomk4naJpjx-CZ1byUZX7zyiklXfLfBBw11-2A";
        String[] data = token.split("[.]");
        Base64.Decoder decoder = Base64.getDecoder();
        // W
        KaKaoUserInfoDto kaKaoUserInfoDto = objectMapper.readValue(decoder.decode(data[1]), KaKaoUserInfoDto.class);
        // T
        assertThat(kaKaoUserInfoDto.getEmail()).isEqualTo("wndlsrnr1@naver.com");
    }
}
