package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }
    public List<Note> getNoteList(Integer userId) {
        return noteMapper.getNoteList(userId);
    }
    public Note getNoteById(Integer noteId) {
        return noteMapper.getNoteById(noteId);
    }
    public Integer createNote(Note note) {
        return noteMapper.createNote(note);
    }
    public Integer updateNote(Note note) {
        return noteMapper.update(note);
    }
    public void deleteNote(Integer noteId) {
        noteMapper.deleteNote(noteId);
    }
}
