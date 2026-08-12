package com.orion.mddapi.services;

import org.springframework.stereotype.Service;
import com.orion.mddapi.repositories.CommentRepository;
import com.orion.mddapi.entities.Comment;
import java.util.List;
import com.orion.mddapi.dto.CommentDto;
import com.orion.mddapi.dto.AuthorDto;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<CommentDto> getCommentsByArticleId(Long articleId) {
        return commentRepository.findAllByArticleIdOrderByCreatedAtAsc(articleId)
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    public CommentDto convertToDto(Comment comment) {
        AuthorDto authorDto = new AuthorDto(comment.getAuthor().getId(), comment.getAuthor().getUsername());
        return new CommentDto(comment.getId(), comment.getContent(), authorDto, comment.getCreatedAt());
    }
}
