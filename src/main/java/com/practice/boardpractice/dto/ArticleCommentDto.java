package com.practice.boardpractice.dto;

import com.practice.boardpractice.domain.Article;
import com.practice.boardpractice.domain.ArticleComment;
import com.practice.boardpractice.domain.UserAccount;

import java.time.LocalDateTime;

/**
 * A DTO for the {@link com.practice.boardpractice.domain.ArticleComment} entity
 */
public record ArticleCommentDto(
        Long id,
        Long articleId,
        UserAccountDto userAccountDto,
        Long parentCommentId,
        String content,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {
    public static ArticleCommentDto of(Long articleId,
                                       UserAccountDto userAccountDto,
                                       String content) {
        return ArticleCommentDto.of(articleId,
                userAccountDto,
                null,
                content);
    }

    public static ArticleCommentDto of(Long articleId,
                                       UserAccountDto userAccountDto,
                                       Long parentCommentId,
                                       String content) {
        return ArticleCommentDto.of(null,
                articleId,
                userAccountDto,
                parentCommentId,
                content,
                null,
                null,
                null,
                null);
    }

    public static ArticleCommentDto of(Long id,
                                       Long articleId,
                                       UserAccountDto userAccountDto,
                                       Long parentCommentId,
                                       String content,
                                       LocalDateTime createdAt,
                                       String createdBy,
                                       LocalDateTime modifiedAt,
                                       String modifiedBy) {
        return new ArticleCommentDto(id,
                articleId,
                userAccountDto,
                parentCommentId,
                content,
                createdAt,
                createdBy,
                modifiedAt,
                modifiedBy);
    }

    public static ArticleCommentDto from(ArticleComment entity) {
        return new ArticleCommentDto(
                entity.getId(),
                entity.getArticle().getId(),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getParentCommentId(),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }

    public ArticleComment toEntity(Article entity, UserAccount userAccount) {
        return ArticleComment.of(
                entity,
                userAccount,
                content
        );
    }
}