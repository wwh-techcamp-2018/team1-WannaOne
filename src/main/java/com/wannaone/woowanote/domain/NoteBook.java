package com.wannaone.woowanote.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;

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

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToMany
    @JoinTable(
            name = "shared_note_book",
            joinColumns = @JoinColumn(name = "note_book_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> peers = new ArrayList<>();

    @OneToMany(mappedBy = "noteBook")
    @JsonManagedReference
    @OrderBy("updateDatetime DESC")
    @Where(clause = "deleted = false")
    private List<Note> notes = new ArrayList<>();

    @ColumnDefault(value = "false")
    private boolean deleted;

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public NoteBook(String title) {
        this.title = title;
        this.deleted = false;
    }

    public NoteBook(Long id, String title) {
        this.id = id;
        this.title = title;
        this.deleted = false;
    }

    public boolean isNoteBookOwner(User compareUser) {
        return this.owner.getId().equals(compareUser.getId());
    }

    public void addNote(Note note) {
        this.notes.add(note);
    }

    public void addPeer(User loginUser) {
        this.peers.add(loginUser);
    }

    public void delete() {
        this.deleted = true;
    }

    public void removeNote(Note note) {
        this.notes.remove(note);
    }
}
