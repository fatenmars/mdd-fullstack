package com.orion.mddapi.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.orion.mddapi.services.ArticleService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import com.orion.mddapi.dto.ArticleDto;
import com.orion.mddapi.dto.CreateArticleRequest;
import java.util.List;
import com.orion.mddapi.dto.ArticleDetailDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public List<ArticleDto> getFeed(@RequestParam(name = "order", defaultValue = "desc") String order) {
        return articleService.getFeed(order);
    }

    @GetMapping("/{articleId}")
    public ArticleDetailDto getArticleDetail(@PathVariable Long articleId) {
        return articleService.getArticleDetail(articleId);
    }

    @PostMapping
    public ArticleDto createArticle(@Valid @RequestBody CreateArticleRequest request) {
        return articleService.createArticle(request);
    }

}
