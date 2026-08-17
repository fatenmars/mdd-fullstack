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
import com.orion.mddapi.security.AuthenticatedUserProvider;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final AuthenticatedUserProvider authenticatedUserProvider;

    public UserService(UserRepository userRepository, SubscriptionRepository subscriptionRepository,
            AuthenticatedUserProvider authenticatedUserProvider) {
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.authenticatedUserProvider = authenticatedUserProvider;
    }

    public UserProfileDto getCurrentUserProfile() {
        User currentUser = this.authenticatedUserProvider.getCurrentUser();
        List<ThemeDto> subscriptions = this.subscriptionRepository.findAllByUserId(currentUser.getId()).stream()
                .map(sub -> new ThemeDto(sub.getTheme().getId(), sub.getTheme().getTitle())).toList();

        UserProfileDto userProfile = new UserProfileDto(currentUser.getEmail(), currentUser.getUsername(),
                subscriptions);

        return userProfile;

    }

    public UserProfileDto updateProfile(UpdateProfileRequest request) {
        User currentUser = this.authenticatedUserProvider.getCurrentUser();
        currentUser.setEmail(request.email());
        currentUser.setUsername(request.username());
        if (request.password() != null && !request.password().isBlank()) {
            currentUser.setPassword(passwordEncoder.encode(request.password()));
        }
        userRepository.save(currentUser);
        return getCurrentUserProfile();
    }

}
