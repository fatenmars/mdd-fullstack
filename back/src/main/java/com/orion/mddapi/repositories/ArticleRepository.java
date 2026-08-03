package com.orion.mddapi.repositories;

import com.orion.mddapi.entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {

}
