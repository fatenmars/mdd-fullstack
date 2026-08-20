package com.orion.mddapi.repositories;

import com.orion.mddapi.entities.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findAllByUserId(Long userId);

    boolean existsByUserIdAndThemeId(Long userId, Long themeId);

    Optional<Subscription> findByUserIdAndThemeId(Long userId, Long themeId);
}
