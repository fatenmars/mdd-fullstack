package com.orion.mddapi.services;

import java.util.List;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.orion.mddapi.dto.SubscriptionThemeDto;
import com.orion.mddapi.dto.UpdateProfileRequest;
import com.orion.mddapi.dto.UserProfileDto;import om.orion.mddapi.exceptions.UserAlreadyExistsException;
import com.orion.mddapi.entities.User;import import o m.orion.mddapi.repositories.UserRepository;
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
        List<SubscriptionThemeDto> subscriptions = this.subscriptionRepository.findAllByUserId(currentUser.getId())
                .stream()
                .map(sub -> new SubscriptionThemeDto(
                        sub.getTheme().getId(),
                        sub.getTheme().getTitle(),
                        sub.getTheme().getDescription()))
                .toList();

        UserProfileDto userProfile = new UserProfileDto(currentUser.getEmail(), currentUser.getUsername(),
                subscriptions);

        return userProfile;

    }

    public UserProfileDto updateProfile(UpdateProfileRequest request) {
        User currentUser = this.authenticatedUserProvider.getCurrentUser();

        if (!currentUser.getEmail().equals(request.email())
                && userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException("Cet e-mail est déjà utilisé.");
        }
        if (!currentUser.getUsername().equals(request.username())
                && userRepository.existsByUsername(request.username())) {
            throw new UserAlreadyExistsException("Ce nom d'utilisateur est déjà utilisé.");
        }

        currentUser.setEmail(request.email());
        currentUser.setUsername(request.username());
        if (request.password() != null && !request.password().isBlank()) {
            currentUser.setPassword(passwordEncoder.encode(request.password()));
        }
        userRepository.save(currentUser);
        return getCurrentUserProfile();
    }

}
