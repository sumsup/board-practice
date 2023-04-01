package com.practice.boardpractice.request;

import com.practice.boardpractice.dto.ArticleDto;
import com.practice.boardpractice.dto.UserAccountDto;

/**
 * 게시물 등록을 위한 DTO 역할을 함.
 * @param title
 * @param content
 * @param hashtag
 */
public record ArticleRequest(
        String title,
        String content,
        String hashtag
) {
    public static ArticleRequest of(String title, String content, String hashtag) {
        return new ArticleRequest(title, content, hashtag);
    }

    public ArticleDto toDto(UserAccountDto userAccountDto) {
        return ArticleDto.of(
                userAccountDto,
                title,
                content,
                hashtag
        );
    }

}
