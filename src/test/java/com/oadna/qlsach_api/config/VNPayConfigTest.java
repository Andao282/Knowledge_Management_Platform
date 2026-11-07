package com.oadna.qlsach_api.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VNPayConfigTest {

    @Test
    void hmacSHA512_isDeterministicAndHexEncoded() {
        String first = VNPayConfig.hmacSHA512("secret-key", "vnp_Amount=100000&vnp_Command=pay");
        String second = VNPayConfig.hmacSHA512("secret-key", "vnp_Amount=100000&vnp_Command=pay");

        assertThat(first).isEqualTo(second);
        assertThat(first).hasSize(128).matches("[0-9a-f]+");
    }

    @Test
    void hmacSHA512_changesWhenKeyOrDataChanges() {
        String base = VNPayConfig.hmacSHA512("secret-key", "data");

        assertThat(VNPayConfig.hmacSHA512("another-key", "data")).isNotEqualTo(base);
        assertThat(VNPayConfig.hmacSHA512("secret-key", "other-data")).isNotEqualTo(base);
    }

    @Test
    void hmacSHA512_returnsEmptyStringForNullInput() {
        assertThat(VNPayConfig.hmacSHA512(null, "data")).isEmpty();
        assertThat(VNPayConfig.hmacSHA512("secret-key", null)).isEmpty();
    }
}
