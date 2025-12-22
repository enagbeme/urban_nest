package com.urbannest.backend.controller;

import com.urbannest.backend.entity.Property;
import com.urbannest.backend.entity.User;
import com.urbannest.backend.repository.UserRepository;
import com.urbannest.backend.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Collections;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class WebController {

    @Autowired
    private PropertyService propertyService;
    
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            model.addAttribute("user", user);
        }
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/profile")
    public String profile(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof User)) {
            return "redirect:/login";
        }
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/property/{id}")
    public String propertyDetails(@PathVariable UUID id, Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            model.addAttribute("user", user);
        }
        
        Property property = propertyService.getPropertyById(id);
        if (property != null) {
            model.addAttribute("property", property);
            if (property.getOwnerId() != null) {
                userRepository.findById(property.getOwnerId()).ifPresent(owner -> {
                    model.addAttribute("owner", owner);
                });
            }
            return "property-details";
        }
        return "redirect:/client-dashboard";
    }

    @GetMapping("/client-dashboard")
    public String clientDashboard(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof User)) {
            return "redirect:/login";
        }
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        List<Property> properties = propertyService.getAllProperties();
        model.addAttribute("properties", properties);
        return "client-dashboard";
    }

    @GetMapping("/homeowner-dashboard")
    public String homeownerDashboard(Model model, Authentication authentication) {
        log.info("Accessing homeowner dashboard");
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof User)) {
            return "redirect:/login";
        }

        User user = (User) authentication.getPrincipal();
        log.info("Authenticated user: {}", user.getEmail());
        model.addAttribute("user", user);

        List<Property> properties = propertyService.getPropertiesByOwner(user.getId());
        if (properties == null) {
            properties = Collections.emptyList();
        }
        log.info("Properties for user {}: {}", user.getEmail(), properties.size());
        model.addAttribute("properties", properties);

        long activeListings = properties.stream()
                .filter(Property::isAvailable)
                .count();
        model.addAttribute("activeListings", activeListings);

        return "homeowner-dashboard";
    }
}
