package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }
    public boolean isFileAvailable(Integer userId, String filename){
        return fileMapper.getFileByName(userId,filename) == null;
    }
    public Integer uploadFile(File file) {
        return fileMapper.uploadFile(file);
    }
    public List<File> getFileList(Integer userId) {
        return fileMapper.getFileList(userId);
    }
    public File getFileById(Integer fileId) {
        return fileMapper.getFileById(fileId);
    }
    public void deleteFile(Integer fileId) {
        fileMapper.deleteFile(fileId);
    }
}
