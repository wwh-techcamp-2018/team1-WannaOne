package com.wannaone.woowanote.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class Comment extends AuditingDateEntity {
    private static final long serialVersionUID = -7885300229018261642L;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "writer_id")
    private User writer;

    @ManyToOne
    @JoinColumn(name = "note_id")
    @JsonBackReference
    private Note note;

    public void addWriter(User writer) {
        this.writer = writer;
    }

    public Comment(String content, Note note) {
        this.content = content;
        this.note = note;
    }
}
