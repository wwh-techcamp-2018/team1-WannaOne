package com.wannaone.woowanote.service;

import com.wannaone.woowanote.domain.Note;
import com.wannaone.woowanote.domain.NoteBook;
import com.wannaone.woowanote.domain.User;
import com.wannaone.woowanote.exception.RecordNotFoundException;
import com.wannaone.woowanote.repository.NoteRepository;
import com.wannaone.woowanote.support.NewNoteNotificationMessageSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NoteServiceTest {
    @Mock
    private NoteRepository noteRepository;
    @Mock
    private NoteBookService noteBookService;
    @Mock
    private NewNoteNotificationMessageSender newNoteNotificationMessageSender;
    @InjectMocks
    private NoteService noteService;

    @Test
    public void getNote_success() {
        Note testNote = new Note("우아한 노트 너무 좋아요!", "이렇게 좋은 노트가 무료라니 믿기지 않아요.");
        when(noteRepository.findByIdAndDeletedIsFalse(1L)).thenReturn(Optional.of(testNote));

        Note note = noteService.getNote(1L);
        assertThat(note).isEqualTo(testNote);
    }

    @Test(expected = RecordNotFoundException.class)
    public void getNote_fail_when_not_found() {
        when(noteRepository.findByIdAndDeletedIsFalse(1L)).thenThrow(new RecordNotFoundException());
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
    public void createNewNote() {
        NoteBook testNoteBook = new NoteBook("노트북1");
        User writer = User.defaultUser();
        when(noteBookService.getNoteBookById(3L)).thenReturn(testNoteBook);
        assertThat(noteService.save(3L, writer).getTitle()).isEqualTo("나의 우아한 노트");
        assertThat(noteService.save(3L, writer).getText()).isEmpty();
    }

    @Test
    public void updateNote() {
        Note originalNote = new Note("새로운 노트", "잘 저장되고 있나요?");
        when(noteRepository.findByIdAndDeletedIsFalse(1L)).thenReturn(Optional.of(originalNote));

        Note updateNote = new Note("수정된 노트", "잘 수정되고 있나요?");

        assertThat(noteService.updateNote(1L, updateNote)).isEqualTo(originalNote.update(updateNote));
        assertThat(originalNote.getTitle()).isEqualTo(updateNote.getTitle());
        assertThat(originalNote.getText()).isEqualTo(updateNote.getText());
        assertThat(originalNote.getUpdateDatetime()).isEqualTo(updateNote.getUpdateDatetime());
    }

    @Test
    public void deleteNote() {
        Note originalNote = new Note("삭제할 노트", "잘 삭제될거죠?");
        when(noteRepository.findByIdAndDeletedIsFalse(1L)).thenReturn(Optional.of(originalNote));
        Note deletedNote = noteService.deleteNote(1L);
        assertThat(deletedNote.isDeleted()).isTrue();
    }

    //TODO 삭제된건 제외하고 들고오게 하기
}