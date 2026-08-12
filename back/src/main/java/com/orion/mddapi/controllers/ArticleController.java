package com.orion.mddapi.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.orion.mddapi.services.ArticleService;
import org.springframework.web.bind.annotation.GetMapping;
import com.orion.mddapi.dto.ArticleDto;
import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public List<ArticleDto> getFeed() {
        return articleService.getFeed();
    }

}
