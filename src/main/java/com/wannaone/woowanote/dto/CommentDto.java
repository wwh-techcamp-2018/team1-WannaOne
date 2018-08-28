package com.wannaone.woowanote.dto;

import com.wannaone.woowanote.domain.Comment;
import com.wannaone.woowanote.domain.Note;
import com.wannaone.woowanote.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class CommentDto {
    private Long id;

    @NotBlank
    private String content;

    private User writer;

    private boolean isWriter;

    public CommentDto(String content) {
        this.content = content;
    }

    public CommentDto(Comment comment, User loginUser) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.writer = comment.getWriter();
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
