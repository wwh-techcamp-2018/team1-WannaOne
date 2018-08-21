package com.wannaone.woowanote.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
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

    @OneToMany(mappedBy = "note")
    //순환 참조 해결, 개발 채널에서 공유된 내용 참고
    @JsonManagedReference
    private List<Comment> comments = new ArrayList<>();

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public Note(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public Note(Long id, String title, String text) {
        this.id = id;
        this.title = title;
        this.text = text;
    }
}
