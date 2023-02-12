package com.practice.boardpractice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    @GetMapping
    public String articles(ModelMap map) {
        map.addAttribute("articles", new ArrayList<>());


        return "articles/index";
    }

    @GetMapping("/{articleId}")
    public String article(@PathVariable Long articleId, ModelMap map) {
        map.addAttribute("article", "article"); // todo : 구현할때 데이터 넣어줘야 함.
        map.addAttribute("articlesComments", new ArrayList<>());

        return "articles/detail";
    }
}
