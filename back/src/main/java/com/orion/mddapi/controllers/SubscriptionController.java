package com.orion.mddapi.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.orion.mddapi.services.SubscriptionService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/users/me/themes")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/{themeId}")
    public void subscribe(@PathVariable Long themeId) {
        subscriptionService.subscribe(themeId);
    }

    @DeleteMapping("/{themeId}")
    public void unsubscribe(@PathVariable Long themeId) {
        subscriptionService.unsubscribe(themeId);
    }

}
