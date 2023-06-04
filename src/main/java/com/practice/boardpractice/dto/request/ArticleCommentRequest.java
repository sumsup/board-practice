package com.practice.boardpractice.dto.request;

import com.practice.boardpractice.dto.ArticleCommentDto;
import com.practice.boardpractice.dto.UserAccountDto;

public record ArticleCommentRequest(
        Long articleId,
        Long parentCommentId,
        String content
) {
    public static ArticleCommentRequest of(Long articleId,
                                           String content) {
        return ArticleCommentRequest.of(articleId,
                null,
                content);
    }

    public static ArticleCommentRequest of(Long articleId,
                                           Long parentCommentId,
                                           String content) {
        return new ArticleCommentRequest(articleId,
                parentCommentId,
                content);
    }

    public ArticleCommentDto toDto(UserAccountDto userAccountDto) {
        return ArticleCommentDto.of(
                articleId,
                userAccountDto,
                parentCommentId,
                content
        );
    }

}
