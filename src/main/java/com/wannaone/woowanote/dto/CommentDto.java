package com.wannaone.woowanote.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wannaone.woowanote.common.LocalDateTimeDeserializer;
import com.wannaone.woowanote.common.LocalDateTimeSerializer;
import com.wannaone.woowanote.domain.Comment;
import com.wannaone.woowanote.domain.Note;
import com.wannaone.woowanote.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentDto {
    private Long id;

    @NotBlank
    private String content;

    private User writer;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime registerDatetime;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updateDatetime;

    private boolean isWriter;

    public CommentDto(String content) {
        this.content = content;
    }

    public CommentDto(Comment comment, User loginUser) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.writer = comment.getWriter();
        this.registerDatetime = comment.getRegisterDatetime();
        this.updateDatetime = comment.getUpdateDatetime();
        this.isWriter = matchWriter(loginUser);
    }

    public Comment toEntity(Note note) {
        return new Comment(this.content, note);
    }

    public static CommentDto fromEntity(Comment comment, User loginUser) {
        return new CommentDto(comment, loginUser);
    }

    public boolean getIsWriter() {
        return isWriter;
    }

    public boolean matchWriter(User loginUser) {
        return writer.equals(loginUser);
    }
}
