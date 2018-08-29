package com.wannaone.woowanote.repository;

import com.wannaone.woowanote.domain.Invitation;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InvitationRepository extends CrudRepository<Invitation, Long> {
    List<Invitation> findByGuestId(Long guestId);
}
