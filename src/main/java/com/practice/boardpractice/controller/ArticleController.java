package com.practice.boardpractice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    @GetMapping
    public String articles(ModelMap map) {
        map.addAttribute("articles", new ArrayList<>());
        map.addAttribute("articlesComments", new ArrayList<>());

        return "articles/index";
    }
}
