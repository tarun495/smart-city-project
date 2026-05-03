package com.smartcity.smartcity.service;

import com.smartcity.smartcity.model.Business;
import com.smartcity.smartcity.repository.BusinessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BusinessService {

    @Autowired
    private BusinessRepository businessRepository;

    public List<Business> getAllBusinesses() {
        return businessRepository.findAll();
    }

    public List<Business> getByCategory(String category) {
        return businessRepository.findByCategory(category);
    }

    public List<Business> getFeaturedBusinesses() {
        return businessRepository.findByIsFeatured(true);
    }

    public Business saveBusiness(Business business) {
        return businessRepository.save(business);
    }

    public Business getById(Long id) {
        return businessRepository.findById(id).orElse(null);
    }

    public void deleteBusiness(Long id) {
        businessRepository.deleteById(id);
    }
}