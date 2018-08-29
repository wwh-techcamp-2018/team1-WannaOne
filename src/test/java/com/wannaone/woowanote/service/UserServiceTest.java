package com.wannaone.woowanote.service;

import com.wannaone.woowanote.domain.Invitation;
import com.wannaone.woowanote.domain.NoteBook;
import com.wannaone.woowanote.domain.User;
import com.wannaone.woowanote.dto.*;
import com.wannaone.woowanote.exception.InvalidInvitationException;
import com.wannaone.woowanote.exception.UnAuthenticationException;
import com.wannaone.woowanote.repository.InvitationRepository;
import com.wannaone.woowanote.repository.UserRepository;
import com.wannaone.woowanote.support.InvitationMessageSender;
import com.wannaone.woowanote.support.InvitationStatus;
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
import java.util.List;
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
    private InvitationService invitationService;
    @Mock
    private NoteBookService noteBookService;
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
    public void precheckInvitationValidityTest() {
        InvitationPrecheckingDto nonduplicatePrecheckingDto = new InvitationPrecheckingDto("dooho@woowahan.com", 3L);
        User guest = new User("dooho@woowahan.com", "123", "dooho");
        NoteBook noteBook1 = new NoteBook(1L, guest, "noteBook1");
        NoteBook noteBook2 = new NoteBook(2L, guest, "noteBook2");
        guest.addSharedNoteBook(noteBook1, noteBook2);
        when(userRepository.findByEmail("dooho@woowahan.com")).thenReturn(Optional.ofNullable(guest));
        assertThat(userService.precheckInvitationValidity(nonduplicatePrecheckingDto).getName()).isEqualTo("dooho");
    }

    @Test(expected = InvalidInvitationException.class)
    public void precheckInvitationValidityTest_when_duplicate() {
        InvitationPrecheckingDto duplicatePrecheckingDto = new InvitationPrecheckingDto("dooho@woowahan.com", 1L);
        User guest = new User("dooho@woowahan.com", "123", "dooho");
        NoteBook noteBook1 = new NoteBook(1L, guest, "noteBook1");
        NoteBook noteBook2 = new NoteBook(2L, guest, "noteBook2");
        guest.addSharedNoteBook(noteBook1, noteBook2);
        when(userRepository.findByEmail("dooho@woowahan.com")).thenReturn(Optional.ofNullable(guest));
        userService.precheckInvitationValidity(duplicatePrecheckingDto);
    }

    @Test
    public void createInvitationTest() {
        Long hostId = 1L;
        Long guestId = 2L;
        Long notebookId = 3L;
        User host = User.defaultUser();
        User guest = new User("dooho@woowahan.com", "123", "dooho");
        NoteBook notebook = new NoteBook(1L, host, "noteBook");

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(host));
        when(userRepository.findById(2L)).thenReturn(Optional.ofNullable(guest));
        when(noteBookService.getNoteBookById(3L)).thenReturn(notebook);


        Invitation invitation = userService.createInvitation(hostId, guestId, notebookId);
        assertThat(invitation.getHost()).isEqualTo(host);
        assertThat(invitation.getGuest()).isEqualTo(guest);
        assertThat(invitation.getNoteBook()).isEqualTo(notebook);
        assertThat(invitation.getStatus()).isEqualTo(InvitationStatus.PENDING);
    }

    @Test
    public void inviteTest() {
        Long hostId = 1L;
        Long guestId = 2L;
        Long notebookId = 3L;
        User host = new User(hostId, "dooho@woowahan.com", "123");
        User guest = new User(guestId, "kyunam@woowahan.com", "234");
        NoteBook notebook = new NoteBook(notebookId, host, "noteBook");

        List<Long> guestIdList = Arrays.asList(2L);
        InvitationDto invitationDto = new InvitationDto(guestIdList, notebookId);

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(host));
        when(userRepository.findById(2L)).thenReturn(Optional.ofNullable(guest));
        when(noteBookService.getNoteBookById(3L)).thenReturn(notebook);

        userService.invite(host, invitationDto);

        Invitation invitation = userService.createInvitation(hostId, guestId, notebookId);
        verify(invitationService).save(invitation);
    }


    @Test
    public void addSharedNoteBookTest() {
        NoteBook testNoteBook = new NoteBook(1L, "노트북1");
        User user = new User(1L, "유저", "1234");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(noteBookService.getNoteBookById(1L)).thenReturn(testNoteBook);
        userService.addSharedNoteBook(user, 1L);
        assertThat(testNoteBook.getPeers().contains(user)).isTrue();
    }

    @Test
    public void searchLikeUserNameTest() {
        User firstUser = new User(1L, "유저1", "1234");
        User secondUser = new User(2L, "유저2", "1234");
        InvitationGuestDto firstInvitationGuestDto = new InvitationGuestDto(firstUser);
        InvitationGuestDto secondInvitationGuestDto = new InvitationGuestDto(secondUser);

        when(userRepository.findByEmailLike("%유저%")).thenReturn(Arrays.asList(firstUser, secondUser));
        assertThat(userService.searchEmailLike("유저", secondUser).contains(firstInvitationGuestDto));
    }

    @Test
    public void getInvitationsTest() {
        Long hostId = 1L;
        Long guestId = 2L;
        Long notebookId = 3L;
        User host = new User(hostId, "dooho@woowahan.com", "123", "dooho");
        User guest = new User(guestId, "kyunam@woowahan.com", "234", "kyunam");
        NoteBook notebook = new NoteBook(notebookId, host, "noteBook");
        List<Long> guestIdList = Arrays.asList(2L);

        Invitation invitation = new Invitation(6L, host, guest, notebook);
        when(invitationService.getInvitationsByGuestId(2L)).thenReturn(Arrays.asList(invitation));

        assertThat(userService.getInvitations(guest).contains(new NotificationMessageDto(invitation))).isTrue();
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