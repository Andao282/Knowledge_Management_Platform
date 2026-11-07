package com.oadna.qlsach_api.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private final JwtService jwtService = new JwtService();

    @Test
    void generateToken_producesValidSignedTokenWithClaims() {
        String token = jwtService.generateToken("an", "ROLE_ADMIN");

        assertThat(token).isNotBlank();
        assertThat(token.split("\\.")).hasSize(3);
        assertThat(jwtService.extractUsername(token)).isEqualTo("an");
        assertThat(jwtService.extractRole(token)).isEqualTo("ROLE_ADMIN");
    }

    @Test
    void generateToken_embedsDifferentRoles() {
        String staffToken = jwtService.generateToken("staff01", "ROLE_STAFF");

        assertThat(jwtService.extractUsername(staffToken)).isEqualTo("staff01");
        assertThat(jwtService.extractRole(staffToken)).isEqualTo("ROLE_STAFF");
    }
}
