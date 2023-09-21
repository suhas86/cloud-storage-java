package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {
    private CredentialMapper credentialMapper;

    public CredentialService(CredentialMapper credentialMapper) {
        this.credentialMapper = credentialMapper;
    }
    public Integer createCredential(Credential credential) {
        return credentialMapper.createCredential(credential);
    }
    public List<Credential> getCredentialList(Integer userId) {
        return credentialMapper.getCredentialList(userId);
    }
    public Credential getCredentialById(Integer credentialId) {
        return credentialMapper.getCredentialById(credentialId);
    }
    public Integer updateCredential(Credential credential) {
        return credentialMapper.updateCredential(credential);
    }
    public void deleteCredential(Integer credentialId) {
        credentialMapper.deleteCredential(credentialId);
    }
}
