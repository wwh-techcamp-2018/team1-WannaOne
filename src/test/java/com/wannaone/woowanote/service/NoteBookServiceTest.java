package com.wannaone.woowanote.service;

import com.wannaone.woowanote.domain.NoteBook;
import com.wannaone.woowanote.domain.User;
import com.wannaone.woowanote.repository.NoteBookRepository;
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
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NoteBookServiceTest {
    @Mock
    private NoteBookRepository noteBookRepository;
    @Mock
    private UserService userService;

    @InjectMocks
    private NoteBookService noteBookService;

    @Test
    public void getAllNoteBook_success() {
        User loginUser = new User(1L, "doy@woowahan.com", "1234");

        NoteBook testNoteBook1 = new NoteBook("노트북1");
        NoteBook testNoteBook2 = new NoteBook("노트북2");
        loginUser.addNoteBook(testNoteBook1);
        loginUser.addNoteBook(testNoteBook2);

        when(userService.findUserById(1L)).thenReturn(loginUser);

        List<NoteBook> noteList = noteBookService.getNoteBooksByOwnerId(loginUser.getId());
        assertThat(noteList).containsAll(Arrays.asList(testNoteBook1, testNoteBook2));
    }

    @Test
    public void getAllNoteBook_success_when_list_empty() {
        User loginUser = new User(1L, "doy@woowahan.com", "1234");
        when(userService.findUserById(loginUser.getId())).thenReturn(loginUser);
        List<NoteBook> noteBookList = noteBookService.getNoteBooksByOwnerId(loginUser.getId());
        assertThat(noteBookList).isEmpty();
    }

    @Test
    public void getNoteBookByNoteBookId() {
        NoteBook testNoteBook = new NoteBook("노트북1");
        when(noteBookRepository.findById(1L)).thenReturn(Optional.of(testNoteBook));

        NoteBook noteBook = noteBookService.getNoteBookByNoteBookId(1L);
        assertThat(noteBook).isEqualTo(testNoteBook);
    }

    @Test
    public void deleteNoteBookByNoteBookId() {
        NoteBook testNoteBook = new NoteBook("노트북1");

        User user = new User(1L, "유저", "1234");
        testNoteBook.setOwner(user);

        when(noteBookRepository.findById(1L)).thenReturn(Optional.of(testNoteBook));

        assertThat(noteBookService.delete(1L, user).isDeleted()).isEqualTo(true);
    }

    @Test
    public void getNoteBooksByPeerIdTest() {
        User loginUser = new User(1L, "doy@woowahan.com", "1234");
        NoteBook testNoteBook = new NoteBook("노트북1");
        loginUser.addSharedNoteBook(testNoteBook);
        when(userService.findUserById(1L)).thenReturn(loginUser);
        assertThat(noteBookService.getNoteBooksByPeerId(1L)).contains(testNoteBook);
    }
}
