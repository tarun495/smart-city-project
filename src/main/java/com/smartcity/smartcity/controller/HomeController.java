package com.smartcity.smartcity.controller;

import com.smartcity.smartcity.service.BusinessService;
import com.smartcity.smartcity.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private BusinessService businessService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("latestNews",
            newsService.getFreeArticles());
        model.addAttribute("featuredBusinesses",
            businessService.getFeaturedBusinesses());
        return "index";
    }

    @GetMapping("/history")
    public String history() {
        return "history/history";
    }

    @GetMapping("/search")
    public String search(
            @RequestParam(required = false) String q,
            Model model) {
        model.addAttribute("query", q);
        if (q != null && !q.trim().isEmpty()) {
            model.addAttribute("newsResults",
                newsService.getAllNews().stream()
                    .filter(n -> n.getTitle()
                        .toLowerCase()
                        .contains(q.toLowerCase()))
                    .toList());
            model.addAttribute("businessResults",
                businessService.getAllBusinesses().stream()
                    .filter(b ->
                        b.getName().toLowerCase()
                            .contains(q.toLowerCase()) ||
                        b.getCategory().toLowerCase()
                            .contains(q.toLowerCase()))
                    .toList());
        } else {
            model.addAttribute("newsResults", List.of());
            model.addAttribute("businessResults", List.of());
        }
        return "search/search";
    }

    // ── PUBLIC Hash Generator ─────────────────────────────
    // Visit: http://localhost:8080/hash
    // NO login needed
    @GetMapping("/hash")
    @ResponseBody
    public String generateHash(
            @RequestParam(defaultValue = "admin123")
                String pwd) {
        String hash = passwordEncoder.encode(pwd);
        return "<html><body>" +
            "<h2>BCrypt Hash Generator</h2>" +
            "<p><b>Password:</b> " + pwd + "</p>" +
            "<p><b>Hash:</b><br/>" +
            "<textarea rows='3' cols='80' " +
                "onclick='this.select()'>" +
                hash +
            "</textarea></p>" +
            "<hr/>" +
            "<p>Usage: " +
            "<a href='/hash?pwd=admin123'>/hash?pwd=admin123</a> | " +
            "<a href='/hash?pwd=test123'>/hash?pwd=test123</a> | " +
            "<a href='/hash?pwd=smartcity123'>" +
                "/hash?pwd=smartcity123</a>" +
            "</p>" +
            "</body></html>";
    }
}