package com.practice.boardpractice.repository.querydsl;

import com.practice.boardpractice.domain.Article;
import com.practice.boardpractice.domain.QArticle;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class ArticleRepositoryCustomImpl extends QuerydslRepositorySupport implements ArticleRepositoryCustom {

    public ArticleRepositoryCustomImpl() {
        super(Article.class);
    }

    @Override
    public List<String> findAllDistinctHashtags() {
        QArticle article = QArticle.article; // TODO : 자동생성 Entity. 어떻게 자동 생성 되는가?

        // queryDSL로 실행.
        JPQLQuery<String> query = from(article)
                .distinct()
                .select(article.hashtag)
                .where(article.hashtag.isNotNull());

        return query.fetch();

        // 아래와 같이 inline variable 할 수 있음.
//        return from(article)
//                .distinct()
//                .select(article.hashtag)
//                .where(article.hashtag.isNotNull())
//                .fetch();
    }

}
