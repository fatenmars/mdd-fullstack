package com.orion.mddapi.dto;

import java.time.LocalDateTime;

public record ArticleDto(
        Long id,
        String title,
        String content,
        LocalDateTime createdAt,
        AuthorDto author,
        ThemeDto theme) {
}