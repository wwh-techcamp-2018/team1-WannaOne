package com.wannaone.woowanote.service;

import com.wannaone.woowanote.domain.User;
import com.wannaone.woowanote.dto.LoginDto;
import com.wannaone.woowanote.dto.UserDto;
import com.wannaone.woowanote.exception.UnAuthenticationException;
import com.wannaone.woowanote.exception.UserDuplicatedException;
import com.wannaone.woowanote.repository.UserRepository;
import com.wannaone.woowanote.validation.ValidationMessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Resource(name = "bCryptPasswordEncoder")
    private PasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private MessageSourceAccessor msa;

    public User save(UserDto userDto) {
        if (isExistUser(userDto.getEmail())) {
            throw new UserDuplicatedException(msa.getMessage("email.duplicate.message"));
        }
        return userRepository.save(userDto.toEntityWithPasswordEncode(bCryptPasswordEncoder));
    }

    public User login(LoginDto loginDto) {
        return userRepository.findByEmail(loginDto.getEmail())
                .filter(u -> u.matchPassword(loginDto, bCryptPasswordEncoder))
                .orElseThrow(() -> new UnAuthenticationException(msa.getMessage("unauthentication.invalid.message")));
    }

    public boolean isExistUser(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
