package com.wannaone.woowanote.repository;

import com.wannaone.woowanote.domain.Note;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends CrudRepository<Note, Long> {
    @Override
    List<Note> findAll();

    Optional<Note> findByIdAndDeletedIsFalse(Long id);
}
