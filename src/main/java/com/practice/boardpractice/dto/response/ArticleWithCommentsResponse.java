package com.practice.boardpractice.dto.response;

import com.practice.boardpractice.dto.ArticleCommentDto;
import com.practice.boardpractice.dto.ArticleWithCommentsDto;
import com.practice.boardpractice.dto.HashtagDto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public record ArticleWithCommentsResponse(
        Long id,
        String title,
        String content,
        Set<String> hashtags,
        LocalDateTime createdAt,
        String email,
        String nickname,
        String userId,
        Set<ArticleCommentResponse> articleCommentsResponse
) implements Serializable {

    public static ArticleWithCommentsResponse of(Long id,
                                                 String title,
                                                 String content,
                                                 Set<String> hashtags,
                                                 LocalDateTime createdAt,
                                                 String email,
                                                 String nickname,
                                                 String userId,
                                                 Set<ArticleCommentResponse> articleCommentResponses) {
        return new ArticleWithCommentsResponse(id,
                title,
                content,
                hashtags,
                createdAt,
                email,
                nickname,
                userId,
                articleCommentResponses);
    }

    public static ArticleWithCommentsResponse from(ArticleWithCommentsDto dto) {
        String nickname = dto.userAccountDto().nickname();
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.userAccountDto().userId();
        }

        return new ArticleWithCommentsResponse(
                dto.id(),
                dto.title(),
                dto.content(),
                dto.hashtagDtos().stream().map(HashtagDto::hashtagName).collect(Collectors.toUnmodifiableSet()),
                dto.createdAt(),
                dto.userAccountDto().email(),
                nickname,
                dto.userAccountDto().userId(),
                organizeChildComments(dto.articleCommentDtos())
        );
    }

    /**
     * 댓글과 대댓글을 계층구조 형태로 반환.
     * @param comments
     * @return 정리된 대댓글.
     */
    private static Set<ArticleCommentResponse> organizeChildComments(Set<ArticleCommentDto> comments) {
        // 댓글을 ID와 DTO 자체를 key, value로 조합해서 Map 으로 만듬.
        Map<Long, ArticleCommentResponse> map = comments.stream()
                .map(ArticleCommentResponse::from)
                .collect(Collectors.toMap(ArticleCommentResponse::id, Function.identity()));

        // 그 맵에서 부모댓글에 해당하는 id를 가진 DTO를 가져와서 자식댓글을 할당함.
        map.values().stream()
                .filter(ArticleCommentResponse::hasParentComment)
                .forEach(comment -> {
                    ArticleCommentResponse parentComment = map.get(comment.parentCommentId());
                    parentComment.childComments().add(comment);
                });

        // 부모댓글이 없는 것들만 추출해서 Map으로 맵핑함.
        return map.values().stream()
                .filter(comment -> !comment.hasParentComment())
                .collect(Collectors.toCollection(() ->
                        // Set으로 나가지만 순서를 정렬.
                        // TreeSet에 Comparator를 인자로 전달하면 정렬을 지정할 수 있다.
                        new TreeSet<>(Comparator
                                // 작성일을 내림차순정렬.
                                .comparing(ArticleCommentResponse::createdAt)
                                .reversed()
                                // 다음으로 id 를 오름차순으로 정렬한다.
                                .thenComparingLong(ArticleCommentResponse::id)
                        )
                ));
    }

}
