package com.website.utils.common;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class SearchAfterEncoderTest {

    String str1 = "str1";
    String str2 = "str2";

    @Test
    void encode() {
        String encode = SearchAfterEncoder.encode(str1, str2);
        assertThat(encode).isNotNull();
    }

    @Test
    void decode() {
        String encode = SearchAfterEncoder.encode(str1, str2);

        String[] decode = SearchAfterEncoder.decode(encode);
        assertThat(decode).contains("str1", "str2");
    }

    @Test
    void decodeSingle() {
        final String encode = SearchAfterEncoder.encode(str1, str2);

        assertThatThrownBy(
                () -> SearchAfterEncoder.decodeSingle(encode)
        ).isInstanceOf(RuntimeException.class);

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> SearchAfterEncoder.decodeSingle(encode));
        assertThat(runtimeException.getMessage()).contains("must be 1");

        String encode2 = SearchAfterEncoder.encode(str1);
        String decode = SearchAfterEncoder.decodeSingle(encode2);
        assertThat(decode).contains("str1");
    }

}