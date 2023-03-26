package com.practice.boardpractice.controller;

import com.practice.boardpractice.config.SecurityConfig;
import com.practice.boardpractice.dto.ArticleWithCommentsDto;
import com.practice.boardpractice.dto.UserAccountDto;
import com.practice.boardpractice.service.ArticleService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 게시글")
@Import(SecurityConfig.class) // Security 설정이 MockMvcRequest 설정을 통과시켜 줌. anyRequest().permitAll() 했으니까.
@WebMvcTest(ArticleController.class) // 입력된 컨트롤러만 로딩해서 테스트 수행.
class ArticleControllerTest {

    private final MockMvc mvc;

    @MockBean private ArticleService articleService;

    // 일반 클래스에서는 생성자 파라미터가 한개인 경우 @Autowired를 생략할 수 있지만, 테스트 클래스에서는 명시해 줘야 함.
    ArticleControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 정상 호출")
    @Test
    void givenNothing_whenRequestingArticlesView_thenReturnsArticlesView() throws Exception {
        // Given.
        given(articleService.searchArticles(eq(null), eq(null), any(Pageable.class))).willReturn(Page.empty());

        // When & Then.
        mvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index")) // view 있는지 이름으로 검증.
                .andExpect(model().attributeExists("articles")); // articles 데이터 유무를 확인.
//                .andExpect(model().attributeExists("searchTypes"));

        // should()는 verify(1)이랑 같다. 즉, 해당 메서드가 '1회 호출 됐음'이랑 같다는 의미.
        // should() is Alias to verify(mock, times(1));
        then(articleService).should().searchArticles(eq(null), eq(null), any(Pageable.class));

    }

    @DisplayName("[view][GET] 게시글 상세 페이지 - 정상 호출")
    @Test
    void givenNothing_whenRequestingArticleView_thenReturnsArticleView() throws Exception {
        // Given.
        long articleId = 1L;
        given(articleService.getArticle(articleId)).willReturn(createArtcileWithCommentsDto());

        // When & Then.
        mvc.perform(get("/articles/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/detail")) // view 있는지 이름으로 검증.
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attributeExists("articlesComments"));

        then(articleService).should().getArticle(articleId); // 컨트롤러에서는 이런식으로 특정 메서드가 호출됐는지로 확인하는 식으로 구현.
    }


    @Disabled("구현 중")
    @DisplayName("[view][GET] 게시글 검색 전용 페이지 - 정상 호출")
    @Test
    void givenNothing_whenRequestingArticleSearchView_thenReturnsArticleSearchView() throws Exception {
        // Given.

        // When & Then.
        mvc.perform(get("/articles/search"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }

    @Disabled("구현 중")
    @DisplayName("[view][GET] 게시글 해시태그 검색 전용 페이지 - 정상 호출")
    @Test
    void givenNothing_whenRequestingArticleHashtagSearchView_thenReturnsArticleHashtagSearchView() throws Exception {
        // Given.

        // When & Then.
        mvc.perform(get("/articles/serach-hashtag"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }

    private ArticleWithCommentsDto createArtcileWithCommentsDto() {
        return ArticleWithCommentsDto.of(
                1L,
                createUserAccountDto(),
                Set.of(),
                "title",
                "content",
                "#java",
                LocalDateTime.now(),
                "kim",
                LocalDateTime.now(),
                "kim"
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                1L,
                "kimMS",
                "password",
                "kimMS@mail.com",
                "kimMS",
                "This is memo",
                LocalDateTime.now(),
                "kimMS",
                LocalDateTime.now(),
                "kimMS"
        );
    }

}