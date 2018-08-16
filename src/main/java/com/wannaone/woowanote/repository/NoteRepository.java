package com.wannaone.woowanote.repository;

import com.wannaone.woowanote.domain.Note;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NoteRepository extends CrudRepository<Note, Long> {
    @Override
    List<Note> findAll();
}
