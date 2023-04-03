package com.practice.boardpractice.repository;

import com.practice.boardpractice.domain.Article;
import com.practice.boardpractice.domain.UserAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("testdb")
@DisplayName("JPA 연결 테스트")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(ArticleRepositoryTest.TestJpaConfig.class) // Auditing 설정 추가해 주기 위해 Import.
@DataJpaTest // @Transactional 포함.
class ArticleRepositoryTest {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;
    @Autowired
    private UserAccountRepository userAccountRepository;

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
        UserAccount userAccount = UserAccount.of("star", "1234", "com@nv.com", "김", null);
        // user정보도 저장해줘야 article을 저장할 때 에러가 안남.
        // user 객체를 저장하지 않은 상태로 article에 user객체를 할당하고 저장하려고하면 아래와 같이 exception 발생.
        // userAccount 먼저 저장하라고 함.
        // TransientPropertyValueException: Not-null property references a transient value - transient instance must be saved before current operation
        userAccountRepository.save(userAccount);

        // when. save한 객체를 리턴 받을 수 있음.
        Article savedArticle = articleRepository.saveAndFlush(Article.of(userAccount,
                "new article",
                "new content",
                "#spring"));

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

    @EnableJpaAuditing
    @TestConfiguration
    public static class TestJpaConfig {
        @Bean
        public AuditorAware<String> auditorAware() {
            return () -> Optional.of("userKim");
        }
    }
}