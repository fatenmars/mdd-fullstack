package com.orion.mddapi.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ArticleDetailDto(Long id, String title, String content, LocalDateTime createdAt, AuthorDto author,
        ThemeDto theme,
        List<CommentDto> comments) {
}