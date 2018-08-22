package com.wannaone.woowanote.web;

import com.wannaone.woowanote.dto.CommentDto;
import com.wannaone.woowanote.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notes/{noteId}/comments")
public class ApiCommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity post(@PathVariable Long noteId, @RequestBody CommentDto commentDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.save(commentDto, noteId));
    }
}
