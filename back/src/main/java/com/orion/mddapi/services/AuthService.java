package com.orion.mddapi.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.orion.mddapi.dto.LoginRequest;
import com.orion.mddapi.dto.RegisterRequest;
import com.orion.mddapi.entities.User;
import com.orion.mddapi.exceptions.InvalidCredentialsException;
import com.orion.mddapi.exceptions.UserAlreadyExistsException;
import com.orion.mddapi.repositories.UserRepository;
import com.orion.mddapi.security.JwtService;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public void register(RegisterRequest request) {
        User newUser = new User();
        if (this.userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException("Cet e-mail est déjà utilisé.");
        }
        if (this.userRepository.existsByUsername(request.username())) {
            throw new UserAlreadyExistsException("Cet username est déjà utilisé.");
        }
        newUser.setEmail(request.email());
        newUser.setUsername(request.username());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        this.userRepository.save(newUser);
    }

    public String login(LoginRequest request) {
        User user = userRepository.findByEmailOrUsername(request.identifier(), request.identifier())
                .orElseThrow(() -> new InvalidCredentialsException("Identifiants invalides."));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Identifiants invalides.");
        }

        return jwtService.generateToken(user.getUsername());
    }

}
