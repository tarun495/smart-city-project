package com.smartcity.smartcity.controller;

import com.smartcity.smartcity.model.NewsArticle;
import com.smartcity.smartcity.model.Business;
import com.smartcity.smartcity.service.BusinessService;
import com.smartcity.smartcity.service.NewsService;
import com.smartcity.smartcity.service.UserService;
import com.smartcity.smartcity.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support
    .RedirectAttributes;
import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private BusinessService businessService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // ── Password Generator (TEMP) ─────────────────────────
    @GetMapping("/generate-password")
    @ResponseBody
    public String generatePassword() {
        String[] passwords = {
            "admin123", "smartcity123",
            "test123", "password123"
        };
        StringBuilder sb = new StringBuilder();
        sb.append("<h2>Password Hashes</h2>");
        for (String pwd : passwords) {
            String hash = passwordEncoder.encode(pwd);
            sb.append("<p><b>").append(pwd)
              .append("</b><br/>").append(hash)
              .append("</p><hr/>");
        }
        return sb.toString();
    }

    // ── Dashboard ─────────────────────────────────────────
    @GetMapping
    public String dashboard(Model model,
                            Principal principal) {
        System.out.println("ADMIN ACCESSED BY: " +
            (principal != null ?
                principal.getName() : "ANONYMOUS"));

        model.addAttribute("totalUsers",
            userService.getAllUsers().size());
        model.addAttribute("totalNews",
            newsService.getAllNews().size());
        model.addAttribute("totalBusinesses",
            businessService.getAllBusinesses().size());
        model.addAttribute("pricing",
            subscriptionService.getPricing());
        return "admin/dashboard";
    }

    // ── User Management ───────────────────────────────────
    @GetMapping("/users")
    public String manageUsers(Model model) {
        model.addAttribute("users",
            userService.getAllUsers());
        return "admin/users";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(
            @PathVariable Long id,
            RedirectAttributes redirectAttrs) {
        try {
            userService.deleteUser(id);
            redirectAttrs.addFlashAttribute("success",
                "User deleted successfully!");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error",
                "Cannot delete: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    // ── News Management ───────────────────────────────────
    @GetMapping("/news")
    public String manageNews(Model model) {
        model.addAttribute("articles",
            newsService.getAllNews());
        model.addAttribute("newArticle",
            new NewsArticle());
        return "admin/news";
    }

    @PostMapping("/news/add")
    public String addNews(
            @ModelAttribute("newArticle")
                NewsArticle article,
            RedirectAttributes redirectAttrs) {
        newsService.saveArticle(article);
        redirectAttrs.addFlashAttribute("success",
            "Article added successfully!");
        return "redirect:/admin/news";
    }

    @GetMapping("/news/delete/{id}")
    public String deleteNews(
            @PathVariable Long id,
            RedirectAttributes redirectAttrs) {
        newsService.deleteArticle(id);
        redirectAttrs.addFlashAttribute("success",
            "Article deleted successfully!");
        return "redirect:/admin/news";
    }

    @GetMapping("/news/edit/{id}")
    public String editNewsForm(
            @PathVariable Long id, Model model) {
        model.addAttribute("article",
            newsService.getById(id));
        return "admin/news-edit";
    }

    @PostMapping("/news/edit/{id}")
    public String editNews(
            @PathVariable Long id,
            @ModelAttribute NewsArticle article,
            RedirectAttributes redirectAttrs) {
        article.setId(id);
        newsService.saveArticle(article);
        redirectAttrs.addFlashAttribute("success",
            "Article updated successfully!");
        return "redirect:/admin/news";
    }

    // ── Business Management ───────────────────────────────
    @GetMapping("/business")
    public String manageBusiness(Model model) {
        model.addAttribute("businesses",
            businessService.getAllBusinesses());
        model.addAttribute("newBusiness",
            new Business());
        return "admin/business";
    }

    @PostMapping("/business/add")
    public String addBusiness(
            @ModelAttribute("newBusiness")
                Business business,
            RedirectAttributes redirectAttrs) {
        businessService.saveBusiness(business);
        redirectAttrs.addFlashAttribute("success",
            "Business added successfully!");
        return "redirect:/admin/business";
    }

    @GetMapping("/business/delete/{id}")
    public String deleteBusiness(
            @PathVariable Long id,
            RedirectAttributes redirectAttrs) {
        businessService.deleteBusiness(id);
        redirectAttrs.addFlashAttribute("success",
            "Business deleted successfully!");
        return "redirect:/admin/business";
    }

    @GetMapping("/business/edit/{id}")
    public String editBusinessForm(
            @PathVariable Long id, Model model) {
        model.addAttribute("business",
            businessService.getById(id));
        return "admin/business-edit";
    }

    @PostMapping("/business/edit/{id}")
    public String editBusiness(
            @PathVariable Long id,
            @ModelAttribute Business business,
            RedirectAttributes redirectAttrs) {
        business.setId(id);
        businessService.saveBusiness(business);
        redirectAttrs.addFlashAttribute("success",
            "Business updated successfully!");
        return "redirect:/admin/business";
    }

    // ── Subscriptions ─────────────────────────────────────
    @GetMapping("/subscriptions")
    public String viewSubscriptions(Model model) {
        model.addAttribute("pricing",
            subscriptionService.getPricing());
        return "admin/subscriptions";
    }
}