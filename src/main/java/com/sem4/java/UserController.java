package com.sem4.java;

import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @PostMapping("/register")
    public String register(@RequestParam String name, @RequestParam String email) {
        return "User Registered: " + name + " | Email: " + email;
    }
}
