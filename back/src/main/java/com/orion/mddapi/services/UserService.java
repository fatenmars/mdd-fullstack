package com.orion.mddapi.services;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.orion.mddapi.dto.ThemeDto;
import com.orion.mddapi.dto.UpdateProfileRequest;
import com.orion.mddapi.dto.UserProfileDto;
import com.orion.mddapi.entities.User;
import com.orion.mddapi.repositories.SubscriptionRepository;
import com.orion.mddapi.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository, SubscriptionRepository subscriptionRepository) {
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public UserProfileDto getCurrentUserProfile() {
        User currentUser = this.userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Utilisateur courant introuvable"));
        List<ThemeDto> subscriptions = this.subscriptionRepository.findAllByUserId(currentUser.getId()).stream()
                .map(sub -> new ThemeDto(sub.getTheme().getId(), sub.getTheme().getTitle())).toList();

        UserProfileDto userProfile = new UserProfileDto(currentUser.getEmail(), currentUser.getUsername(),
                subscriptions);

        return userProfile;

    }

    public UserProfileDto updateProfile(UpdateProfileRequest request) {
        User currentUser = this.userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Utilisateur courant introuvable"));
        currentUser.setEmail(request.email());
        currentUser.setUsername(request.username());
        if (request.password() != null && !request.password().isBlank()) {
            currentUser.setPassword(passwordEncoder.encode(request.password()));
        }
        userRepository.save(currentUser);
        return getCurrentUserProfile();
    }

}
