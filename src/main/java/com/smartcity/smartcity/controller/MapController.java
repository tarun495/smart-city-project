package com.smartcity.smartcity.controller;

import com.smartcity.smartcity.model.Business;
import com.smartcity.smartcity.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/map")
public class MapController {

    @Autowired
    private BusinessService businessService;

    @GetMapping
    public String cityMap(Model model) {
        model.addAttribute("businesses",
            businessService.getAllBusinesses());
        return "map/citymap";
    }

    // REST API for live map markers
    @GetMapping("/api/locations")
    @ResponseBody
    public List<Business> getLocations() {
        return businessService.getAllBusinesses();
    }
}