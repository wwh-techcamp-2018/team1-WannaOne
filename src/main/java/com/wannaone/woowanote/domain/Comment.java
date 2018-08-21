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
    
    private String content;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "writer_id")
    private User writer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_id")
    //순환 참조 해결 예전에 개발 채널에서 공유된 내용 참고
    @JsonBackReference
    private Note note;

    public Comment(String content, Note note) {
        this.content = content;
        this.note = note;
    }
}
