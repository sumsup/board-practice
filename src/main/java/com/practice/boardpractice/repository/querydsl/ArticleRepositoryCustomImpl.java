package com.practice.boardpractice.repository.querydsl;

import com.practice.boardpractice.domain.Article;
import com.practice.boardpractice.domain.QArticle;
import com.practice.boardpractice.domain.QHashtag;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.Collection;
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
                .select(article.hashtags.any().hashtagName);

        return query.fetch();

        // 아래와 같이 inline variable 할 수 있음.
//        return from(article)
//                .distinct()
//                .select(article.hashtag)
//                .where(article.hashtag.isNotNull())
//                .fetch();
    }

    @Override
    public Page<Article> findByHashtagNames(Collection<String> hashtagNames, Pageable pageable) {
        QHashtag hashtag = QHashtag.hashtag;
        QArticle article = QArticle.article;

        JPQLQuery<Article> query = from(article)
                .innerJoin(article.hashtags, hashtag)
                .where(hashtag.hashtagName.in(hashtagNames));
        List<Article> articles = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(articles, pageable, query.fetchCount());
    }

}
