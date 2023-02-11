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

@RepositoryRestResource
public interface ArticleCommentRepository extends
        JpaRepository<ArticleComment, Long>,
        QuerydslPredicateExecutor<ArticleComment>,  // 이거 하나만 추가해도 검색 기능이 추가 된다.
        QuerydslBinderCustomizer<QArticleComment> { // 커스텀 기능을 추가 하기 위해서 추가.

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
