package com.ds.chat.controller;

import com.ds.chat.dto.UserRegistrationDto;
import com.ds.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserRegistrationDto userDto, Model model) {
        // Validate passwords match
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            model.addAttribute("passwordError", "Passwords do not match");
            return "register";
        }

        try {
            userService.registerNewUser(
                    userDto.getUsername(),
                    userDto.getPassword(),
                    userDto.getDisplayName()
            );
            return "redirect:/login?registered";
        } catch (IllegalArgumentException e) {
            model.addAttribute("usernameError", "Username already exists");
            return "register";
        }
    }
    
    @PostMapping("/check-username")
    @ResponseBody
    public ResponseEntity<?> checkUsernameAvailability(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        boolean exists = userService.usernameExists(username);
        return ResponseEntity.ok(Map.of("available", !exists));
    }
}