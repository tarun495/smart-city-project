package com.smartcity.smartcity.controller;

import com.smartcity.smartcity.model.ForumPost;
import com.smartcity.smartcity.service.ForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@Controller
@RequestMapping("/forum")
public class ForumController {

    @Autowired
    private ForumService forumService;

    @GetMapping
    public String forumPage(Model model) {
        model.addAttribute("posts",
            forumService.getAllTopLevelPosts());
        return "forum/forum";
    }

    @PostMapping("/post")
    public String createPost(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam String category,
            Principal principal) {

        ForumPost post = new ForumPost();
        post.setTitle(title);
        post.setContent(content);
        post.setCategory(category);

        forumService.createPost(post, principal.getName());
        return "redirect:/forum";
    }
}