package com.orion.mddapi.repositories;

import com.orion.mddapi.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByArticleIdOrderByCreatedAtAsc(Long articleId);
}
