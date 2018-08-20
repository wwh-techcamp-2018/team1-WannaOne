package com.wannaone.woowanote.web;

import com.wannaone.woowanote.dto.CommentDto;
import com.wannaone.woowanote.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notes/{noteId}/comments")
public class ApiCommentController {
    private CommentService commentService;
    @PostMapping
    public ResponseEntity post(@PathVariable Long noteId, CommentDto commentDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.save(commentDto, noteId));
    }
}
