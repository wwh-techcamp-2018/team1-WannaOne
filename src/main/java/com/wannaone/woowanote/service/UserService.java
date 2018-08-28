package com.wannaone.woowanote.service;

import com.wannaone.woowanote.domain.Invitation;
import com.wannaone.woowanote.domain.NoteBook;
import com.wannaone.woowanote.domain.User;
import com.wannaone.woowanote.dto.*;
import com.wannaone.woowanote.exception.RecordNotFoundException;
import com.wannaone.woowanote.exception.UnAuthenticationException;
import com.wannaone.woowanote.exception.UnAuthorizedException;
import com.wannaone.woowanote.exception.UserDuplicatedException;
import com.wannaone.woowanote.exception.*;
import com.wannaone.woowanote.repository.InvitationRepository;
import com.wannaone.woowanote.repository.UserRepository;
import com.wannaone.woowanote.support.InvitationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InvitationRepository invitationRepository;
    @Autowired
    private NoteBookService noteBookService;
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

    public InvitationGuestDto precheckInvitationValidity(InvitationPrecheckingDto precheckingDto) {
        User guest = findUserByEmail(precheckingDto.getGuestEmail());
        if (guest.hasSharedNotebook(precheckingDto.getNotebookId())) {
            throw new InvalidInvitationException(msa.getMessage("Invalid.invitation"));
        }

        return new InvitationGuestDto(guest);
    }

    public void invite(User host, InvitationDto invitationDto) {
        invitationDto.getGuestIdList().forEach((guestId) -> {
            saveInvitation(createInvitation(host.getId(), guestId, invitationDto.getNotebookId()));
        });
    }

    public Invitation createInvitation(Long hostId, Long guestId, Long notebookId) {
        User host = findUserById(hostId);
        User guest = findUserById(guestId);
        NoteBook noteBook = noteBookService.getNoteBookByNoteBookId(notebookId);
        return new Invitation(host, guest, noteBook);
    }

    public void saveInvitation(Invitation invitation) {
        invitationRepository.save(invitation);
    }

    @Transactional
    public NoteBook addSharedNoteBook(User user, Long noteBookId) {
        User loginUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UnAuthorizedException(msa.getMessage("unauthorized.message")));
        NoteBook sharedNoteBook = noteBookService.getNoteBookByNoteBookId(noteBookId);
        loginUser.addSharedNoteBook(sharedNoteBook);
        sharedNoteBook.addPeer(loginUser);
        return sharedNoteBook;
    }

    public User findUserById(Long userId) {
        return this.userRepository.findById(userId).orElseThrow(() -> new RecordNotFoundException(msa.getMessage("NotFound.user")));
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RecordNotFoundException(msa.getMessage("NotFound.user")));
    }
}
