package com.practice.boardpractice.repository;

import com.practice.boardpractice.domain.ArticleComment;
import com.practice.boardpractice.domain.QArticleComment;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ArticleCommentRepository extends
        JpaRepository<ArticleComment, Long>,
        QuerydslPredicateExecutor<ArticleComment>,  // 이거 하나만 추가해도 검색 기능이 추가 된다.
        QuerydslBinderCustomizer<QArticleComment> { // 커스텀 기능을 추가 하기 위해서 추가.

    /*
     아래와 같이 findByArticle 하고 언더바 Id를 하면, JPA에서 Artcile 테이블의 Id를 조회해서 ArticleComment의 List를
     반환한다.
     이 클래스는 ArticleComment의 Repository이지만, 언더바 Id를 함으로써 연관관계를 타고 들어가서 조회해서 반환해 준다고 함.
     예를 들어 article의 title로 검색하고 싶다고 한다면,
     findByArticle_Title(String title) <-- 이렇게 작성하면 됨.
     findByArticle_ 까지만 하고 ctrl + space하면 자동완성으로 title, Hashtag 등을 보여줌.
    */
    List<ArticleComment> findByArticle_Id(Long articleId);
    void deleteByIdAndUserAccount_UserId(Long articleCommentId, String userId);

    @Override
    default void customize(QuerydslBindings bindings, QArticleComment root) {
        // 검색 기능에서 제외하기 위함.
        bindings.excludeUnlistedProperties(true);
        // 검색 기능에 포함.
        bindings.including(root.content, root.createdAt, root.createdBy);

        // 대소문자 상관 없이
//        bindings.bind(root.title).first(StringExpression::likeIgnoreCase); // like '${v}'
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    }


}
