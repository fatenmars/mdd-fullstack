package com.orion.mddapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterRequest(@Email @NotBlank String email, @NotBlank String username,
        @NotBlank @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{8,}$", message = "Le mot de passe doit faire au moins 8 caractères et contenir une majuscule, une minuscule, un chiffre et un caractère spécial") String password) {

}
