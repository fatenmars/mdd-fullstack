package com.orion.mddapi.repositories;

import com.orion.mddapi.entities.Theme;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThemeRepository extends JpaRepository<Theme, Long> {
}