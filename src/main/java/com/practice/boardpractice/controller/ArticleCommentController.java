package com.practice.boardpractice.controller;

import com.practice.boardpractice.dto.security.BoardPrincipal;
import com.practice.boardpractice.dto.request.ArticleCommentRequest;
import com.practice.boardpractice.service.ArticleCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/comments")
@Controller
public class ArticleCommentController {
    private final ArticleCommentService articleCommentService;

    @PostMapping("/new")
    public String postNewArticleComment(ArticleCommentRequest articleCommentRequest,
                                        @AuthenticationPrincipal BoardPrincipal boardPrincipal) {
        articleCommentService.saveArticleComment(articleCommentRequest.toDto(boardPrincipal.toUserAccountDto()));

        return "redirect:/articles/" + articleCommentRequest.articleId();
    }


    // HTML form은 GET과 POST만 지원하고 있다.
    @PostMapping ("/{commentId}/delete")
    public String deleteArticleComment(@PathVariable Long commentId,
                                       @AuthenticationPrincipal BoardPrincipal boardPrincipal,
                                       Long articleId) {
        articleCommentService.deleteArticleComment(commentId, boardPrincipal.getUsername());

        return "redirect:/articles/" + articleId;
    }
}
