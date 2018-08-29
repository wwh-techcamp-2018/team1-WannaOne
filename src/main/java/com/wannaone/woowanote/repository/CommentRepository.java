package com.wannaone.woowanote.repository;

import com.wannaone.woowanote.domain.Comment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends CrudRepository<Comment, Long> {
    List<Comment> findByNoteIdAndDeletedFalse(Long noteId);
    Optional<Comment> findByIdAndDeletedFalse(Long id);
}
