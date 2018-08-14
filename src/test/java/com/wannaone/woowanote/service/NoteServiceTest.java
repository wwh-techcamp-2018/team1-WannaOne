package com.wannaone.woowanote.service;

import com.wannaone.woowanote.domain.Note;
import com.wannaone.woowanote.exception.RecordNotFoundException;
import com.wannaone.woowanote.repository.NoteRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
}