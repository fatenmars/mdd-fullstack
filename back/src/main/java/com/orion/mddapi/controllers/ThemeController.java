package com.orion.mddapi.controllers;

import com.orion.mddapi.dto.ThemeListItemDto;
import com.orion.mddapi.services.ThemeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public List<ThemeListItemDto> getAllThemes() {
        return themeService.getAllThemes();
    }
}