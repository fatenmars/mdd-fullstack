package com.orion.mddapi.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.orion.mddapi.dto.CommentDto;
import com.orion.mddapi.dto.CreateCommentRequest;
import com.orion.mddapi.services.CommentService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/articles")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{articleId}/comments")
    public CommentDto addComment(@PathVariable Long articleId, @Valid @RequestBody CreateCommentRequest request) {
        return commentService.addComment(articleId, request);
    }
}
