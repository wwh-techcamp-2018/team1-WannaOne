package com.wannaone.woowanote.repository;

import com.wannaone.woowanote.domain.NoteBook;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NoteBookRepository extends CrudRepository<NoteBook, Long> {
    List<NoteBook> findAll();
}
