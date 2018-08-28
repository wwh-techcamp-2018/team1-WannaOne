package com.wannaone.woowanote.service;

import com.wannaone.woowanote.domain.NoteBook;
import com.wannaone.woowanote.domain.User;
import com.wannaone.woowanote.dto.InvitationGuestDto;
import com.wannaone.woowanote.dto.InvitationPrecheckingDto;
import com.wannaone.woowanote.dto.LoginDto;
import com.wannaone.woowanote.dto.UserDto;
import com.wannaone.woowanote.exception.RecordNotFoundException;
import com.wannaone.woowanote.exception.UnAuthenticationException;
import com.wannaone.woowanote.exception.UnAuthorizedException;
import com.wannaone.woowanote.exception.UserDuplicatedException;
import com.wannaone.woowanote.exception.*;
import com.wannaone.woowanote.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
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

    @Transactional
    public NoteBook addSharedNoteBook(User user, Long noteBookId) {
        User loginUser = userRepository.findById(user.getId())
                .orElseThrow(()-> new UnAuthorizedException(msa.getMessage("unauthorized.message")));
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
