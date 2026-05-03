package com.smartcity.smartcity.controller;

import com.smartcity.smartcity.dto.SubscriptionDto;
import com.smartcity.smartcity.service.NewsService;
import com.smartcity.smartcity.service.BusinessService;
import com.smartcity.smartcity.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@Controller
@RequestMapping("/paid")
public class PaidServiceController {

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private BusinessService businessService;

    // ─── SERVICE 1: Premium City News ────────────────────────
    @GetMapping("/news-premium")
    public String premiumNews(Model model, Principal principal) {
        if (!isSubscribed(principal, "NEWS_PREMIUM"))
            return "redirect:/paid/subscribe?service=NEWS_PREMIUM";

        model.addAttribute("articles",
            newsService.getPremiumArticles());
        return "paid/premium-news";
    }

    // ─── SERVICE 2: Live Market Rates ─────────────────────────
    @GetMapping("/market-rates")
    public String marketRates(Model model, Principal principal) {
        if (!isSubscribed(principal, "MARKET_RATES"))
            return "redirect:/paid/subscribe?service=MARKET_RATES";

        // Sample static rates — replace with DB later
        model.addAttribute("rates", getSampleRates());
        return "paid/market-rates";
    }

    // ─── SERVICE 3: Featured Business Listing ─────────────────
    @GetMapping("/business-listing")
    public String businessListing(Model model, Principal principal) {
        if (!isSubscribed(principal, "BUSINESS_LISTING"))
            return "redirect:/paid/subscribe?service=BUSINESS_LISTING";

        model.addAttribute("businesses",
            businessService.getFeaturedBusinesses());
        return "paid/business-listing";
    }

    // ─── SERVICE 4: City Tourist Guide ────────────────────────
    @GetMapping("/city-guide")
    public String cityGuide(Model model, Principal principal) {
        if (!isSubscribed(principal, "CITY_GUIDE"))
            return "redirect:/paid/subscribe?service=CITY_GUIDE";

        return "paid/city-guide";
    }

    // ─── SUBSCRIPTION PAGE ────────────────────────────────────
    @GetMapping("/subscribe")
    public String subscribePage(@RequestParam String service,
                                Model model,
                                Principal principal) {
        // If already subscribed, redirect to service
        if (principal != null && isSubscribed(principal, service)) {
            return "redirect:/paid/" +
                service.toLowerCase().replace("_", "-");
        }

        model.addAttribute("service", service);
        model.addAttribute("pricing",
            subscriptionService.getPricing());
        model.addAttribute("serviceNames",
            getServiceNames());
        return "paid/subscribe";
    }

    // ─── PROCESS PAYMENT ──────────────────────────────────────
    @PostMapping("/subscribe")
    public String processSubscription(
            @RequestParam String serviceType,
            Principal principal) {

        SubscriptionDto dto = new SubscriptionDto();
        dto.setServiceType(serviceType);
        dto.setAmountPaid(
            subscriptionService.getPricing()
                .getOrDefault(serviceType, 0.0));

        subscriptionService.createSubscription(
            principal.getName(), dto);

        // Redirect to the service after payment
        String redirectUrl = "/paid/" +
            serviceType.toLowerCase().replace("_", "-");
        return "redirect:" + redirectUrl;
    }

    // ─── HELPER METHODS ───────────────────────────────────────
    private boolean isSubscribed(Principal principal,
                                  String serviceType) {
        if (principal == null) return false;
        return subscriptionService.hasActiveSubscription(
            principal.getName(), serviceType);
    }

    private java.util.Map<String, String> getServiceNames() {
        java.util.Map<String, String> names =
            new java.util.LinkedHashMap<>();
        names.put("NEWS_PREMIUM",      "Premium City News");
        names.put("MARKET_RATES",      "Live Market Rates");
        names.put("BUSINESS_LISTING",  "Featured Business Listing");
        names.put("CITY_GUIDE",        "City Tourist Guide");
        return names;
    }

    private java.util.Map<String, String> getSampleRates() {
        java.util.Map<String, String> rates =
            new java.util.LinkedHashMap<>();
        rates.put("Tomatoes",  "₹40 / kg");
        rates.put("Onions",    "₹30 / kg");
        rates.put("Potatoes",  "₹25 / kg");
        rates.put("Milk",      "₹55 / litre");
        rates.put("Rice",      "₹60 / kg");
        rates.put("Wheat",     "₹35 / kg");
        rates.put("Gold",      "₹6,200 / gram");
        rates.put("Silver",    "₹75 / gram");
        return rates;
    }
}