package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignupController {
    private UserService userService;
    public SignupController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/signup")
    public String signupPage(@ModelAttribute("createUser") User user){
        return "signup";
    }

    @PostMapping("/signup")
    public String signupUser(@ModelAttribute("createUser") User user, Model model) {
        String username = user.getUsername();
        String firstname = user.getFirstname();
        String lastname = user.getLastname();
        String password = user.getPassword();

        if (username.isEmpty() || firstname.isEmpty() || lastname.isEmpty() || password.isEmpty()) {
            model.addAttribute("error", "All fields are mandatory");
        } else if (!userService.usernameIsAvailable(username)) {
            model.addAttribute("error", "Username already exists");
        } else {
            int rowAdded = userService.createUser(user);
            if (rowAdded < 0) {
                model.addAttribute("error", "Oops something went wrong! Please try again");
            } else {
                model.addAttribute("success", true);
                return "signup";
            }
        }

        return "signup";
    }
}
