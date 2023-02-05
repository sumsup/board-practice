package com.practice.boardpractice.repository;

import com.practice.boardpractice.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
