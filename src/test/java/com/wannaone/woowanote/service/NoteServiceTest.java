package com.wannaone.woowanote.service;

import com.wannaone.woowanote.domain.Note;
import com.wannaone.woowanote.exception.RecordNotFoundException;
import com.wannaone.woowanote.repository.NoteRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteService noteService;

    @Test
    public void getNote_success() {
        Note testNote = new Note("우아한 노트 너무 좋아요!", "이렇게 좋은 노트가 무료라니 믿기지 않아요.");
        when(noteRepository.findById(1L)).thenReturn(Optional.of(testNote));

        Note note = noteService.getNote(1L);
        assertThat(note).isEqualTo(testNote);
    }

    @Test(expected = RecordNotFoundException.class)
    public void getNote_fail_when_not_found() {
        when(noteRepository.findById(1L)).thenThrow(new RecordNotFoundException());
        noteService.getNote(1L);
    }

    @Test
    public void getAllNote_success() {
        Note testNote1 = new Note("우아한 노트 너무 좋아요!", "이렇게 좋은 노트가 무료라니 믿기지 않아요.");
        Note testNote2 = new Note("너무 좋아요 우아한 노트!", "믿기지 않아요 이렇게 좋은 노트가 무료라니.");
        when(noteRepository.findAll()).thenReturn(Arrays.asList(testNote1, testNote2));

        List<Note> noteList = noteService.getAllNotes();
        assertThat(noteList).containsAll(Arrays.asList(testNote1, testNote2));
    }

    @Test
    public void getAllNote_success_when_list_empty() {
        when(noteRepository.findAll()).thenReturn(new ArrayList<>());
        List<Note> noteList = noteService.getAllNotes();
        assertThat(noteList).isEmpty();
    }

    @Test
    public void postNewNote() {
        Note testNote = new Note(1l,"새로운 노트", "잘 저장되고 있나요?");
        when(noteRepository.save(testNote)).thenReturn(testNote);
        assertThat(noteService.postNewNote(testNote)).isEqualTo(testNote);
    }
}