package com.smartcity.smartcity.controller;

import com.smartcity.smartcity.dto.UserRegistrationDto;
import com.smartcity.smartcity.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("user") UserRegistrationDto dto,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) return "auth/register";

        if (userService.usernameExists(dto.getUsername())) {
            model.addAttribute("usernameError", "Username already taken");
            return "auth/register";
        }
        if (userService.emailExists(dto.getEmail())) {
            model.addAttribute("emailError", "Email already registered");
            return "auth/register";
        }

        userService.registerUser(dto);
        return "redirect:/auth/login?registered";
    }
}