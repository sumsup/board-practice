package com.practice.boardpractice.dto.request;

import com.practice.boardpractice.dto.ArticleDto;
import com.practice.boardpractice.dto.HashtagDto;
import com.practice.boardpractice.dto.UserAccountDto;

import java.util.Set;

/**
 * 게시물 등록을 위한 DTO 역할을 함.
 * @param title
 * @param content
 * @param hashtag
 */
public record ArticleRequest(
        String title,
        String content
) {
    public static ArticleRequest of(String title, String content) {
        return new ArticleRequest(title, content);
    }

    public ArticleDto toDto(UserAccountDto userAccountDto) {
        return toDto(userAccountDto, null);
    }

    public ArticleDto toDto(UserAccountDto userAccountDto, Set<HashtagDto> hashtagDtos) {
        return ArticleDto.of(
                userAccountDto,
                title,
                content,
                hashtagDtos
        );
    }

}
