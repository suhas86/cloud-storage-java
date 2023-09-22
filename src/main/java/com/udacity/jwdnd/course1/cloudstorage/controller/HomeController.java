package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    private NoteService noteService;
    private UserService userService;
    private CredentialService credentialService;
    private EncryptionService encryptionService;
    private FileService fileService;

    public HomeController(NoteService noteService, UserService userService, CredentialService credentialService, EncryptionService encryptionService, FileService fileService) {
        this.noteService = noteService;
        this.userService = userService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
        this.fileService = fileService;
    }
    @GetMapping("/home")
    public String homePage(Authentication authentication, Model model, Note note, Credential credential, File file){
        User user = userService.getUser(authentication.getName());
        Integer userId = user.getUserId();

        List<Note> noteList = noteService.getNoteList(userId);
        model.addAttribute("notes", noteList);
        List<Credential> credentialList = credentialService.getCredentialList(userId);
        model.addAttribute("credentials", credentialList);
        model.addAttribute("encryptionService", encryptionService);
        List<File> fileList = fileService.getFileList(
                userId);
        model.addAttribute("files", fileList);
        return "home";
    }
}
