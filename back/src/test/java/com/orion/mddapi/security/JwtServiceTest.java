package com.orion.mddapi.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService("test-secret-de-32-caracteres-minimum-pour-les-tests", 86400000L);
    }

    @Test
    @DisplayName("Le token généré contient le username et est valide")
    void generateToken_thenExtractAndValidate() {
        String token = jwtService.generateToken("bob");

        assertThat(jwtService.extractUsername(token)).isEqualTo("bob");
        assertThat(jwtService.isValid(token)).isTrue();
    }

    @Test
    @DisplayName("Un token invalide n'est pas valide")
    void isValid_invalidToken_returnsFalse() {
        assertThat(jwtService.isValid("token-bidon")).isFalse();
    }
}