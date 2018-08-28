package com.wannaone.woowanote.service;

import com.wannaone.woowanote.domain.NoteBook;
import com.wannaone.woowanote.domain.User;
import com.wannaone.woowanote.dto.InvitationPrecheckingDto;
import com.wannaone.woowanote.dto.LoginDto;
import com.wannaone.woowanote.dto.UserDto;
import com.wannaone.woowanote.exception.InvalidInvitationException;
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
    public void precheckInvitationTest() {
        InvitationPrecheckingDto nonduplicatePrecheckingDto = new InvitationPrecheckingDto("dooho@woowahan.com", 3L);
        User guest = new User("dooho@woowahan.com", "123", "dooho");
        NoteBook noteBook1 = new NoteBook(1L, guest, "noteBook1");
        NoteBook noteBook2 = new NoteBook(2L, guest, "noteBook2");
        guest.addSharedNoteBook(noteBook1, noteBook2);
        when(userRepository.findByEmail("dooho@woowahan.com")).thenReturn(Optional.ofNullable(guest));
        assertThat(userService.precheckInvitation(nonduplicatePrecheckingDto).getName()).isEqualTo("dooho");
    }

    @Test(expected = InvalidInvitationException.class)
    public void precheckInvitationTest_when_duplicate() {
        InvitationPrecheckingDto duplicatePrecheckingDto = new InvitationPrecheckingDto("dooho@woowahan.com", 1L);
        User guest = new User("dooho@woowahan.com", "123", "dooho");
        NoteBook noteBook1 = new NoteBook(1L, guest, "noteBook1");
        NoteBook noteBook2 = new NoteBook(2L, guest, "noteBook2");
        guest.addSharedNoteBook(noteBook1, noteBook2);
        when(userRepository.findByEmail("dooho@woowahan.com")).thenReturn(Optional.ofNullable(guest));
        userService.precheckInvitation(duplicatePrecheckingDto);
    }


    @Test
    public void addSharedNoteBookTest() {
        NoteBook testNoteBook = new NoteBook(1L, "노트북1");
        User user = new User(1L, "유저", "1234");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(noteBookService.getNoteBookByNoteBookId(1L)).thenReturn(testNoteBook);
        userService.addSharedNoteBook(user, 1L);
        assertThat(user.getSharedNotebooks().contains(testNoteBook)).isTrue();
        assertThat(testNoteBook.getPeers().contains(user)).isTrue();

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