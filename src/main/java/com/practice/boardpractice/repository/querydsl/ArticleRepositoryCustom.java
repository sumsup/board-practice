package com.practice.boardpractice.repository.querydsl;

import java.util.List;

// ArticleRepository에서 상속시켜주면 해당 메서드 Impl된 부분을 사용할 수 있음.
// 인터페이스 네임 양식은 Article + Repository + Custom. 이렇게 조합하는게 맞을 거 같음.
public interface ArticleRepositoryCustom {
    List<String> findAllDistinctHashtags();
}
