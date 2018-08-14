package com.wannaone.woowanote.service;

import com.wannaone.woowanote.domain.User;
import com.wannaone.woowanote.dto.UserDto;
import com.wannaone.woowanote.exception.UserDuplicatedException;
import com.wannaone.woowanote.repository.UserRepository;
import com.wannaone.woowanote.validation.ValidationMessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User save(UserDto userDto) {
        if (isExistUser(userDto.getEmail())) {
            throw new UserDuplicatedException(ValidationMessageUtil.USER_DUPLICATION);
        }
        return userRepository.save(userDto.toEntity());
    }

    public boolean isExistUser(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
