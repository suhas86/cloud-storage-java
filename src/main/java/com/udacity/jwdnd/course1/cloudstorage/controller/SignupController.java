package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

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
    public RedirectView signupUser(@ModelAttribute("createUser") User user, Model model, RedirectAttributes redirectAttributes) {
        String username = user.getUsername();
        String firstname = user.getFirstname();
        String lastname = user.getLastname();
        String password = user.getPassword();
        RedirectView signUpRedirect = new RedirectView("/signup",true);
        if (username.isEmpty() || firstname.isEmpty() || lastname.isEmpty() || password.isEmpty()) {
            model.addAttribute("error", "All fields are mandatory");
        } else if (!userService.usernameIsAvailable(username)) {
            redirectAttributes.addFlashAttribute("error", "Username already exists");
        } else {
            int rowAdded = userService.createUser(user);
            if (rowAdded < 0) {
                redirectAttributes.addFlashAttribute("error", "Oops something went wrong! Please try again");
            } else {
                RedirectView redirectView = new RedirectView("/login",true);
                redirectAttributes.addFlashAttribute("signupSuccess",true);
                return redirectView;
            }
        }

        return signUpRedirect;
    }
}
