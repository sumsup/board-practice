package com.practice.boardpractice.repository.querydsl;

import com.practice.boardpractice.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

// ArticleRepository에서 상속시켜주면 해당 메서드 Impl된 부분을 사용할 수 있음.
// 인터페이스 네임 양식은 Article + Repository + Custom. 이렇게 조합하는게 맞을 거 같음.
public interface ArticleRepositoryCustom {
    /**
     * 해시태그 도메인을 새로 만들었으므로. @deprecated
     * @see HashtagRepositoryCustom#findAllHashtagNames()
     */
    @Deprecated
    List<String> findAllDistinctHashtags();

    Page<Article> findByHashtagNames(Collection<String> hashtagNames, Pageable pageable);
}
