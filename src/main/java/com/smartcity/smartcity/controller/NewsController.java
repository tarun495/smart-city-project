package com.smartcity.smartcity.controller;

import com.smartcity.smartcity.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping
    public String newsPage(Model model) {
        model.addAttribute("articles",
            newsService.getFreeArticles());
        return "news/news";
    }

    @GetMapping("/{id}")
    public String newsDetail(
            @PathVariable Long id, Model model) {

        model.addAttribute("article",
            newsService.getById(id));

        // Related news - show 3 free articles
        model.addAttribute("relatedNews",
            newsService.getFreeArticles()
                .stream()
                .filter(a -> !a.getId().equals(id))
                .limit(3)
                .toList());

        return "news/detail";
    }
}