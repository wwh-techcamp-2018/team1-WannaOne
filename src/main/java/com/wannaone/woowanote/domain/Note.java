package com.wannaone.woowanote.domain;

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

    public Note(String title, String text) {
        this(null, title, text, null, null, null);
    }

    public Note(Long id, String title, String text) {
        this(id, title, text, null, null, null);
    }
}
