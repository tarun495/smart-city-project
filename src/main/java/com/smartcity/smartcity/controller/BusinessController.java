package com.smartcity.smartcity.controller;

import com.smartcity.smartcity.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/business")
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    @GetMapping
    public String businessDirectory(Model model) {
        model.addAttribute("businesses",
            businessService.getAllBusinesses());
        model.addAttribute("featured",
            businessService.getFeaturedBusinesses());
        return "business/directory";
    }
}