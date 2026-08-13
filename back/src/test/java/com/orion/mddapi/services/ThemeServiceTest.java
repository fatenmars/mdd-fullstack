package com.orion.mddapi.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.orion.mddapi.entities.Theme;
import com.orion.mddapi.repositories.ThemeRepository;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ThemeServiceTest {

    @Mock
    private ThemeRepository themeRepository;

    @InjectMocks
    private ThemeService themeService;

    @Test
    @DisplayName("La liste des thèmes renvoie tous les thèmes")
    void getAllThemes_returnsAllThemes() {
        // Arrange
        Theme theme1 = new Theme();
        theme1.setId(1L);
        theme1.setTitle("JavaScript");

        Theme theme2 = new Theme();
        theme2.setId(2L);
        theme2.setTitle("Java");

        when(themeRepository.findAll()).thenReturn(List.of(theme1, theme2));

        // Act
        List<Theme> themes = themeService.getAllThemes();

        // Assert
        assertThat(themes).hasSize(2);
        assertThat(themes.get(0).getTitle()).isEqualTo(theme1.getTitle());
    }
}
