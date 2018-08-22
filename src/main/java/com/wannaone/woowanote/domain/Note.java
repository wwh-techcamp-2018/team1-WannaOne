package com.wannaone.woowanote.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Note extends AuditingDateEntity {
    private static final long serialVersionUID = -6987292439817177663L;

    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String text;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "writer_id")
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_book_id")
    @JsonBackReference
    private NoteBook noteBook;

    @OneToMany(mappedBy = "note")
    //순환 참조 해결, 개발 채널에서 공유된 내용 참고
    @JsonManagedReference
    private List<Comment> comments = new ArrayList<>();

    private boolean deleted = false;

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public void addNoteBook(NoteBook noteBook) {
        this.noteBook = noteBook;
    }

    public Note(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public Note(String title, String text, User writer) {
        this.title = title;
        this.text = text;
        this.writer = writer;
    }

    public Note update(Note note) {
        this.title = note.title;
        this.text = note.text;
        return this;
    }

    public Note delete() {
        this.deleted = true;
        return this;
    }

    public void setWriter(User writer) {
        this.writer = writer;
    }
}
