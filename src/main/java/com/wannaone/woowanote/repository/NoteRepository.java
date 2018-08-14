package com.wannaone.woowanote.repository;

import com.wannaone.woowanote.domain.Note;
import org.springframework.data.repository.CrudRepository;

public interface NoteRepository extends CrudRepository<Note, Long> {

}
