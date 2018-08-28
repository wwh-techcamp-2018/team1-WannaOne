package com.wannaone.woowanote.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    @Test
    public void hasSharedNotebook() {
        User user1 = User.defaultUser();
        User user2 = new User("dooho@woowahan.com", "123", "dooho");
        NoteBook noteBook1 = new NoteBook(1L, user1, "noteBook1");
        NoteBook noteBook2 = new NoteBook(2L, user2, "noteBook2");
        user1.addSharedNoteBook(noteBook1, noteBook2);
        assertThat(user1.hasSharedNotebook(1L)).isTrue();
        assertThat(user1.hasSharedNotebook(2L)).isTrue();
        assertThat(user1.hasSharedNotebook(3L)).isFalse();
    }
}