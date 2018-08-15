package com.wannaone.woowanote.service;

import com.wannaone.woowanote.domain.User;
import com.wannaone.woowanote.dto.LoginDto;
import com.wannaone.woowanote.dto.UserDto;
import com.wannaone.woowanote.exception.UnAuthenticationException;
import com.wannaone.woowanote.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    public static final Logger log = LoggerFactory.getLogger(UserServiceTest.class);
    @Mock
    private UserRepository userRepository;
    @Mock
    private MessageSourceAccessor msa;
    @Spy
    private PasswordEncoder mockPasswordEncoder = new MockPasswordEncoder();
    @InjectMocks
    private UserService userService;

    @Test
    public void createTest() {
        UserDto userDto = UserDto.defaultUserDto();
        userService.save(userDto);
        User user = userDto.toEntityWithPasswordEncode(mockPasswordEncoder);
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());
        verify(userRepository).save(user);
    }

    @Test
    public void loginTest() {
        User user = UserDto.defaultUserDto().toEntityWithPasswordEncode(mockPasswordEncoder);
        LoginDto loginDto = LoginDto.defaultLoginDto();
        when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.ofNullable(user));
        when(msa.getMessage("unauthentication.message")).thenReturn("아이디 또는 비밀번호가 잘못되었습니다.");
        assertThat(userService.login(loginDto)).isEqualTo(user);
    }

    @Test(expected = UnAuthenticationException.class)
    public void loginFailureTest() {
        User user = UserDto.defaultUserDto().setPassword("11").toEntityWithPasswordEncode(mockPasswordEncoder);
        LoginDto loginDto = LoginDto.defaultLoginDto();
        when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.ofNullable(user));
        when(msa.getMessage("unauthentication.message")).thenReturn("아이디 또는 비밀번호가 잘못되었습니다.");
        assertThat(userService.login(loginDto)).isEqualTo(user);
    }

    private class MockPasswordEncoder implements PasswordEncoder {
        @Override
        public String encode(CharSequence rawPassword) {
            return new StringBuilder(rawPassword).reverse().toString();
        }

        @Override
        public boolean matches(CharSequence rawPassword, String encodedPassword) {
            return encode(rawPassword).equals(encodedPassword);
        }
    }
}