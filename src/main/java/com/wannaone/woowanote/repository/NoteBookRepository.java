package com.wannaone.woowanote.repository;

import com.wannaone.woowanote.domain.NoteBook;
import com.wannaone.woowanote.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NoteBookRepository extends CrudRepository<NoteBook, Long> {
    List<NoteBook> findByOwnerId(Long ownerId);
    List<NoteBook> findByOwnerIdAndDeletedFalse(Long ownerId);
    List<NoteBook> findByPeersContainingAndDeletedFalse(User peer);
}
