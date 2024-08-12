package com.website.utils.common;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.stream.Collectors;

public class SearchAfterEncoder {

    private static final String regex = ";";

    public static String encode(String... params) {
        if (params == null || params.length == 0) {
            return null;
        }
        String joined = String.join(regex, params);
        return Base64.getUrlEncoder().encodeToString(joined.getBytes(StandardCharsets.UTF_8));
    }

    public static String[] decode(String param) {
        String decoded = new String(Base64.getUrlDecoder().decode(param), StandardCharsets.UTF_8);
        return decoded.split(regex);
    }

    public static String decodeSingle(String param) {
        String[] decode = decode(param);
        if (decode.length != 1) {
            //todo: change into custom exception
            throw new RuntimeException("decoded array length must be 1. length=" + decode.length);
        }
        return decode[0];
    }
}
