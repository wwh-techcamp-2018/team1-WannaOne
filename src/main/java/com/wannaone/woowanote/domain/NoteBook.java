package com.wannaone.woowanote.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class NoteBook implements Serializable {
    private static final long serialVersionUID = 3747495447637624997L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "noteBook")
    @JsonManagedReference
    private List<Note> notes = new ArrayList<>();

    public NoteBook(String title) {
        this.title = title;
    }

    public void addNote(Note note) {
        this.notes.add(note);
    }
}
