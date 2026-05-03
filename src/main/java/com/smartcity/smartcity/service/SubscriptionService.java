package com.smartcity.smartcity.service;

import com.smartcity.smartcity.dto.SubscriptionDto;
import com.smartcity.smartcity.model.Subscription;
import com.smartcity.smartcity.model.User;
import com.smartcity.smartcity.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserService userService;

    public boolean hasActiveSubscription(String username, String serviceType) {
        return subscriptionRepository
            .findByUserUsernameAndServiceTypeAndIsActive(username, serviceType, true)
            .isPresent();
    }

    public void createSubscription(String username, SubscriptionDto dto) {
        User user = userService.findByUsername(username).orElse(null);
        Subscription sub = new Subscription();
        sub.setUser(user);
        sub.setServiceType(dto.getServiceType());
        sub.setAmountPaid(dto.getAmountPaid());
        sub.setStartDate(LocalDate.now());
        sub.setEndDate(LocalDate.now().plusMonths(1)); // 1 month subscription
        sub.setActive(true);
        subscriptionRepository.save(sub);
    }

    public Map<String, Double> getPricing() {
        Map<String, Double> prices = new LinkedHashMap<>();
        prices.put("NEWS_PREMIUM", 99.0);
        prices.put("MARKET_RATES", 149.0);
        prices.put("BUSINESS_LISTING", 499.0);
        prices.put("CITY_GUIDE", 199.0);
        return prices;
    }
}