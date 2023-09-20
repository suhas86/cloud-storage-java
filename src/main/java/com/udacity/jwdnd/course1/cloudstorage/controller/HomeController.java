package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    private NoteService noteService;
    private UserService userService;

    public HomeController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }
    @GetMapping("/home")
    public String homePage(Authentication authentication, Model model, Note note){
        User user = userService.getUser(authentication.getName());
        Integer userId = user.getUserId();

        List<Note> noteList = noteService.getNoteList(userId);
        model.addAttribute("notes", noteList);
        return "home";
    }
}
