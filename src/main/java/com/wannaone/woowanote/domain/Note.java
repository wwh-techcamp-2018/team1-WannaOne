package com.wannaone.woowanote.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String text;

    private Date registerDatetime;

    private Date updateDatetime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="writer_id")
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_book_id")
    @JsonBackReference
    private NoteBook noteBook;

    public Note(Long id, String title, String text) {
        this.id = id;
        this.title = title;
        this.text = text;
    }

    public Note(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public Note(String title, String text, Date updateDatetime) {
        this.title = title;
        this.text = text;
        this.updateDatetime = updateDatetime;
    }

    public Note(Long id, String title, String text, User writer) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.writer = writer;
    }

    public Note update(Note note) {
        this.title = note.title;
        this.text = note.text;
        this.updateDatetime = note.updateDatetime;
        return this;
    }
}
