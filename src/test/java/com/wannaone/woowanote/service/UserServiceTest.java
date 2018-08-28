package com.wannaone.woowanote.service;

import com.wannaone.woowanote.domain.NoteBook;
import com.wannaone.woowanote.domain.User;
import com.wannaone.woowanote.dto.LoginDto;
import com.wannaone.woowanote.dto.UserDto;
import com.wannaone.woowanote.exception.UnAuthenticationException;
import com.wannaone.woowanote.repository.NoteBookRepository;
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

import java.util.Arrays;
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
    private NoteBookService noteBookService;
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
        verify(userRepository).save(user);
    }

    @Test
    public void loginTest() {
        User user = UserDto.defaultUserDto().toEntityWithPasswordEncode(mockPasswordEncoder);
        LoginDto loginDto = LoginDto.defaultLoginDto();
        when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.ofNullable(user));
        assertThat(userService.login(loginDto)).isEqualTo(user);
    }

    @Test(expected = UnAuthenticationException.class)
    public void loginFailureTest() {
        User user = UserDto.defaultUserDto().setPassword("11").toEntityWithPasswordEncode(mockPasswordEncoder);
        LoginDto loginDto = LoginDto.defaultLoginDto();
        assertThat(userService.login(loginDto)).isEqualTo(user);
    }

    @Test
    public void addSharedNoteBookTest() {
        NoteBook testNoteBook = new NoteBook(1L, "노트북1");
        User user = new User(1L, "유저", "1234");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(noteBookService.getNoteBookByNoteBookId(1L)).thenReturn(testNoteBook);
        userService.addSharedNoteBook(user, 1L);
        assertThat(user.getShared().contains(testNoteBook)).isTrue();
        assertThat(testNoteBook.getPeers().contains(user)).isTrue();
    }

    @Test
    public void searchLikeUserNameTest() {
        User firstUser = new User(1L, "유저1", "1234");
        User secondUser = new User(2L, "유저2", "1234");

        when(userRepository.findByEmailLike("유저")).thenReturn(Arrays.asList(firstUser, secondUser));
        assertThat(userService.searchLikeUserName("유저")).contains(firstUser, secondUser);
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