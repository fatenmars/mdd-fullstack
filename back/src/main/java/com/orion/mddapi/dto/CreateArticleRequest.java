package com.orion.mddapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateArticleRequest(@NotBlank String title, @NotBlank String content, @NotNull Long themeId) {
}
