package com.orion.mddapi.services;

import com.orion.mddapi.entities.Theme;
import com.orion.mddapi.repositories.ThemeRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<Theme> getAllThemes() {
        return themeRepository.findAll();
    }
}