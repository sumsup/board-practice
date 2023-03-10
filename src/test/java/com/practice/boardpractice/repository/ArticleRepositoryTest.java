package com.practice.boardpractice.repository;

import com.practice.boardpractice.config.JpaConfig;
import com.practice.boardpractice.domain.Article;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("testdb")
@DisplayName("JPA 연결 테스트")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JpaConfig.class) // Auditing 설정 추가해 주기 위해 Import.
@DataJpaTest // @Transactional 포함.
class ArticleRepositoryTest {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    // 생성자 주입 패턴으로 Autowired가 가능.
    ArticleRepositoryTest(@Autowired ArticleRepository articleRepository,
                          @Autowired ArticleCommentRepository articleCommentRepository) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    @DisplayName("select 테스트")
    @Test
    void givenTestData_whenSelecting_thenWorksFine() {
        // given.

        // when.
        List<Article> articles = articleRepository.findAll();

        // then.
        assertThat(articles).isNotNull().hasSize(123);
    }

    @DisplayName("insert 테스트")
    @Test
    void givenTestData_whenInserting_thenWorksFine() {
        // given.
        long previousCount = articleRepository.count();

        // when. save한 객체를 리턴 받을 수 있음.
        Article savedArticle = articleRepository.save(Article.of("new article", "new content", "#spring"));

        // then.
        assertThat(articleRepository.count()).isEqualTo(previousCount + 1);
    }

    @DisplayName("update 테스트")
    @Test
    void givenTestData_whenUpdating_thenWorksFine() {
        // given.
        Article article = articleRepository.findById(1L).orElseThrow();
        String updatedHashtag = "#springboot";
        article.setHashtag(updatedHashtag);

        // when.
        // #1. save만 하고 끝내면 @Transactional에 의해서 롤백됨. update commit 안됨.
//        Article savedArticle = articleRepository.save(article); // save한 객체를 리턴 받을 수 있음.
        // #2. 그래서 commit 해주기 위해서 saveAndFlush를 한다. 그래도 메서드 종료하고 롤백됨.
        Article savedArticle = articleRepository.saveAndFlush(article);

        // then.
        assertThat(savedArticle).hasFieldOrPropertyWithValue("hashtag", updatedHashtag);
    }

    @DisplayName("delete 테스트")
    @Test
    void givenTestData_whenDeleting_thenWorksFine() {
        // given.
        Article article = articleRepository.findById(1L).orElseThrow();
        long previousCount = articleRepository.count();
        long previousArticleCommentCount = articleCommentRepository.count();
        long deletedCommentsSize = article.getArticleComments().size();

        // when.
        articleRepository.delete(article);

        // then.
        assertThat(articleRepository.count()).isEqualTo(previousCount - 1);
        assertThat(articleCommentRepository.count()).isEqualTo(previousArticleCommentCount - deletedCommentsSize);
    }
}