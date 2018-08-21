package com.wannaone.woowanote.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
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

    @OneToMany(mappedBy = "note")
    //순환 참조 해결, 개발 채널에서 공유된 내용 참고
    @JsonManagedReference
    private List<Comment> comments;

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
