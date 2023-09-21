package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/notes")
public class NoteController {
    private UserService userService;
    private NoteService noteService;
    public NoteController(UserService userService, NoteService noteService) {
        this.userService = userService;
        this.noteService = noteService;
    }

    @PostMapping("/save")
    public String createOrUpdate(@ModelAttribute Note note, Model model, Authentication authentication) {
        User user = userService.getUser(authentication.getName());

        if (note.getNoteId() == null) {
            note.setUserId(user.getUserId());
            noteService.createNote(note);
        } else {
            noteService.updateNote(note);
        }

        model.addAttribute("success", true);
        return "redirect:/result";
    }

    @GetMapping("/delete/{noteid}")
    public String delete(@PathVariable("noteid") Integer noteId) {
        noteService.deleteNote(noteId);
        return "redirect:/result";
    }
}
