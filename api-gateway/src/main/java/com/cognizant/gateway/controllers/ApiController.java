package com.cognizant.gateway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.gateway.services.TokenService;

import reactor.core.publisher.Mono;

@RestController
public class ApiController {

    @Autowired
    private TokenService tokenService;

    // 🔐 admin token
    @GetMapping("/token/admin")
    public Mono<String> getAdminToken() {
        return tokenService.getToken("admin-client");
    }

    // 👤 user token
    @GetMapping("/token/user")
    public Mono<String> getUserToken() {
        return tokenService.getToken("user-client");
    }
}