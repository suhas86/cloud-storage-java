package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping("/files")
public class FileController {
    private FileService fileService;
    private UserService userService;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }
    @PostMapping("/upload")
    public String fileUpload(
            @RequestParam("fileUpload") MultipartFile multipartFile,
            Model model,
            Authentication authentication
    ) throws IOException {
        User user = userService.getUser(authentication.getName());
        String filename = multipartFile.getOriginalFilename();
        Integer userId = user.getUserId();
    if(multipartFile.isEmpty()) {
        model.addAttribute("error", "File is mandatory.");
        model.addAttribute("success", false);
    } else if (!fileService.isFileAvailable(userId, filename)) {
            model.addAttribute("error", "File with this name already exists.");
            model.addAttribute("success", false);
        } else {
            String contentType = multipartFile.getContentType();
            byte[] fileData = multipartFile.getBytes();
            Long fileSize = multipartFile.getSize();

            File file = new File();
            file.setContentType(contentType);
            file.setFileData(fileData);
            file.setFileName(filename);
            file.setFileSize(fileSize.toString());
            file.setUserId(userId);

            fileService.uploadFile(file);
            model.addAttribute("success", true);
        }

        return "result";
    }
    @GetMapping("/view/{fileId}")
    public void viewFile(@PathVariable("fileId") Integer fileId, HttpServletResponse response) throws IOException {
        File file = fileService.getFileById(fileId);

        response.setContentType(file.getContentType());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getFileName() + "\"");
        response.setContentLengthLong(Long.parseLong(file.getFileSize()));

        try (OutputStream ops = response.getOutputStream()) {
            ops.write(file.getFileData());
        } catch (Exception e) {
            // Handle exceptions if needed
        }
    }

    @GetMapping("/delete/{fileId}")
    public String deleteFile(@PathVariable("fileId") Integer fileId) {
        fileService.deleteFile(fileId);
        return "redirect:/result";
    }
}
