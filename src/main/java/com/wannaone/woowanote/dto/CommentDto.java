package com.wannaone.woowanote.dto;

import com.wannaone.woowanote.domain.Comment;
import com.wannaone.woowanote.domain.Note;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private String content;
    public Comment toEntity(Note note) {
        return new Comment(this.content, note);
    }
}
