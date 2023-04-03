package com.practice.boardpractice.service;

import com.practice.boardpractice.domain.Article;
import com.practice.boardpractice.domain.UserAccount;
import com.practice.boardpractice.domain.constant.SearchType;
import com.practice.boardpractice.dto.ArticleDto;
import com.practice.boardpractice.dto.ArticleWithCommentsDto;
import com.practice.boardpractice.repository.ArticleRepository;
import com.practice.boardpractice.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserAccountRepository userAccountRepository;

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {
        if (searchKeyword == null || searchKeyword.isBlank()) {
            return articleRepository.findAll(pageable).map(ArticleDto::from);
        }

        return switch (searchType) {
            case TITLE -> articleRepository.findByTitleContaining(searchKeyword, pageable).map(ArticleDto::from);
            case CONTENT -> articleRepository.findByContentContaining(searchKeyword, pageable).map(ArticleDto::from);
            case ID -> articleRepository.findByUserAccount_UserIdContaining(searchKeyword, pageable).map(ArticleDto::from);
            case NICKNAME -> articleRepository.findByUserAccount_NicknameContaining(searchKeyword, pageable).map(ArticleDto::from);
            case HASHTAG -> articleRepository.findByHashtag("#" + searchKeyword, pageable).map(ArticleDto::from);
        };
    }

    @Transactional(readOnly = true)
    public ArticleWithCommentsDto getArticleWithComments(Long articleId) {
        return articleRepository.findById(articleId).map(ArticleWithCommentsDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId));
    }

    @Transactional(readOnly = true)
    public ArticleDto getArticle(Long articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId));
    }

    public void saveArticle(ArticleDto dto) {
        UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().userId());
        articleRepository.save(dto.toEntity(userAccount));
    }

    public void updateArticle(Long articleId, ArticleDto dto) {
        try {
            Article article = articleRepository.getReferenceById(articleId);
            UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().userId());

            // 게시글의 작성자와 로그인 유저가 일치하면 업데이트 진행.
            if (article.getUserAccount().equals(userAccount)) {
                if (dto.title() != null) {
                    article.setTitle(dto.title()); // java Record에서는 getter setter를 다 만들어주고 dto.title() 이런식으로 호출.
                }
                if (dto.content() != null) {
                    article.setContent(dto.content());
                }
                article.setHashtag(dto.hashtag());
            }

            // Transaction이 끝날때 영속성 컨텍스트가 업데이트를 감지해서 실행 한다.
            // dto의 setter가 호출된 바가 있다면 알아서 update를 실행함.
            // 이 코드는 update를 명시적으로 해줄때는 추가해 줘도 된다.
//            articleRepository.save(article);
        } catch (EntityNotFoundException e) {
            log.warn("게시글 업데이트 실패. 게시글을 수정하는데 필요한 정보를 찾을 수 없습니다 - {}", e.getLocalizedMessage());
        }
    }

    public void deleteArticle(long articleId, String userId) {
        articleRepository.deleteByIdAndUserAccount_UserId(articleId, userId);
    }

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticlesViaHashtag(String hashtag, Pageable pageable) {
        if (hashtag == null || hashtag.isBlank()) {
            return Page.empty(pageable);
        }

        return articleRepository.findByHashtag(hashtag, pageable).map(ArticleDto::from);

        // 위 리턴문은 아래와 같이 풀어 쓸 수 있다.
//        Page<Article> allHashtag = articleRepository.findByHashtag(hashtag, pageable);
//        Page<ArticleDto> articleDtos = allHashtag.map(d -> ArticleDto.from(d));
//        return articleDtos;

    }

    public List<String> getHashtags() {
        return articleRepository.findAllDistinctHashtags();
    }
}
