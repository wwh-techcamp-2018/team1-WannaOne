package com.wannaone.woowanote.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wannaone.woowanote.common.LocalDateTimeDeserializer;
import com.wannaone.woowanote.common.LocalDateTimeSerializer;
import com.wannaone.woowanote.domain.Comment;
import com.wannaone.woowanote.domain.Note;
import com.wannaone.woowanote.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NoteDto {
    private Long id;

    private String title;

    private String text;

    private User writer;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime registerDatetime;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updateDatetime;

    private boolean isWriter;

    private List<CommentDto> comments = new ArrayList<>();

    public NoteDto(Note note, User loginUser) {
        this.id = note.getId();
        this.title = note.getTitle();
        this.text = note.getText();
        this.writer = note.getWriter();
        this.registerDatetime = note.getRegisterDatetime();
        this.updateDatetime = note.getUpdateDatetime();
        this.isWriter = matchWriter(loginUser);
        for (Comment comment : note.getComments()) {
            this.comments.add(CommentDto.fromEntity(comment, loginUser));
        }
    }

    public static NoteDto fromEntity(Note note, User loginUser) {
        return new NoteDto(note, loginUser);
    }

    public boolean getIsWriter() {
        return isWriter;
    }

    public boolean matchWriter(User loginUser) {
        return writer.equals(loginUser);
    }
}
