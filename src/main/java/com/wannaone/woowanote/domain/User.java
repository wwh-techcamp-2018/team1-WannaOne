package com.wannaone.woowanote.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wannaone.woowanote.dto.LoginDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Getter
public class User implements Serializable {
    private static final long serialVersionUID = 7342736640368461848L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    private String name;

    private String photoUrl = "http://mblogthumb2.phinf.naver.net/20150427_261/ninevincent_1430122791768m7oO1_JPEG/kakao_1.jpg?type=w2";

    @OneToMany(mappedBy = "owner")
    @JsonIgnore
    @Where(clause = "deleted = false")
    private List<NoteBook> noteBooks = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "shared_note_book",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "note_book_id")
    )
    @JsonIgnore
    @Where(clause = "deleted = false")
    private List<NoteBook> sharedNotebooks = new ArrayList<>();

    public User(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public void addNoteBook(NoteBook noteBook) {
        this.noteBooks.add(noteBook);
    }

    public void addSharedNoteBook(NoteBook... noteBooks) {
        Arrays.stream(noteBooks).forEach((noteBook) -> this.sharedNotebooks.add(noteBook));
    }

    public static User defaultUser() {
        return new User("defaultUser", "password", "user");
    }

    public boolean matchPassword(LoginDto loginDto, PasswordEncoder bCryptPasswordEncoder) {
        return bCryptPasswordEncoder.matches(loginDto.getPassword(), this.password);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }

    public boolean hasSharedNotebook(Long notebookId) {
        if (sharedNotebooks.isEmpty()) {
            return false;
        }
        return this.sharedNotebooks.stream().anyMatch((noteBook -> noteBook.hasSameId(notebookId)));
    }
}
