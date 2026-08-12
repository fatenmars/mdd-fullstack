package com.orion.mddapi.repositories;

import com.orion.mddapi.entities.Article;
import com.orion.mddapi.entities.Theme;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findAllByThemeInOrderByCreatedAtDesc(List<Theme> themes);
}
