package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Base64;

@Controller
@RequestMapping("/credentials")
public class CredentialController {
    private CredentialService credentialService;
    private UserService userService;
    private EncryptionService encryptionService;

    public CredentialController(CredentialService credentialService, UserService userService, EncryptionService encryptionService) {
        this.credentialService = credentialService;
        this.userService = userService;
        this.encryptionService = encryptionService;
    }

    @PostMapping("/save")
    public String createOrEdit(Model model, Authentication authentication, @ModelAttribute Credential credential){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        String encodeSalt = Base64.getEncoder().encodeToString(salt);
        String encodedPassword = encryptionService.encryptValue(credential.getPassword(), encodeSalt);
        credential.setPassword(encodedPassword);
        credential.setKey(encodeSalt);
        User currentUser = userService.getUser(authentication.getName());
        Integer userId = currentUser.getUserId();
        credential.setUserId(userId);
        if(credential.getCredentialId() == null) {
            credentialService.createCredential(credential);
        } else {
            credentialService.updateCredential(credential);
        }

        model.addAttribute("success", true);
        return "redirect:/result";
    }

    @GetMapping("/delete/{credentialId}")
    public String delete(@PathVariable("credentialId") Integer credentialId) {
        credentialService.deleteCredential(credentialId);
        return "redirect:/result";
    }
}
