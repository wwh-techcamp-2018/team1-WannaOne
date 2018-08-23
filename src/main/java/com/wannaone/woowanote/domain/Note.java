package com.wannaone.woowanote.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;

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

    @ManyToOne
    @JoinColumn(name = "writer_id")
    private User writer;

    @ManyToOne
    @JoinColumn(name = "note_book_id")
    @JsonBackReference
    private NoteBook noteBook;

    @OneToMany(mappedBy = "note")
    @JsonManagedReference
    private List<Comment> comments = new ArrayList<>();

    @ColumnDefault(value = "false")
    private boolean deleted;

    public Note(String title, String text) {
        this(title, text, null);
    }

    public Note(User writer) {
        this("나의 우아한 노트", "", writer);
    }

    public Note(String title, String text, User writer) {
        this.title = title;
        this.text = text;
        this.writer = writer;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public void addNoteBook(NoteBook noteBook) {
        this.noteBook = noteBook;
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
