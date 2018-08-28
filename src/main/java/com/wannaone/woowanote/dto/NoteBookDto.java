package com.wannaone.woowanote.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wannaone.woowanote.domain.Note;
import com.wannaone.woowanote.domain.NoteBook;
import com.wannaone.woowanote.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class NoteBookDto {
    private Long id;

    private String title;

    private User owner;

    private List<NoteDto> notes = new ArrayList<>();

    private List<User> peers = new ArrayList<>();

    private boolean deleted;

    public NoteBookDto(NoteBook noteBook, User loginUser) {
        this.id = noteBook.getId();
        this.title = noteBook.getTitle();
        this.owner = noteBook.getOwner();
        for (Note note : noteBook.getNotes()) {
            this.notes.add(NoteDto.fromEntity(note, loginUser));
        }
        this.peers = noteBook.getPeers();
        this.deleted = noteBook.isDeleted();
    }

    public static NoteBookDto fromEntity(NoteBook noteBook, User loginUser) {
        return new NoteBookDto(noteBook, loginUser);
    }

    @JsonIgnore
    public int getPeersSize() {
        return this.peers.size();
    }
}
