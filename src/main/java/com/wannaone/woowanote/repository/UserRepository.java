package com.wannaone.woowanote.repository;

import com.wannaone.woowanote.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository <User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByEmailLike(String emailPattern);
}
