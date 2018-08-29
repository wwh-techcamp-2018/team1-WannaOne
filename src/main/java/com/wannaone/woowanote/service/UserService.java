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
import com.wannaone.woowanote.repository.UserRepository;
import com.wannaone.woowanote.support.InvitationMessageSender;
import com.wannaone.woowanote.support.InvitationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InvitationService invitationService;
    @Autowired
    private NoteBookService noteBookService;
    @Resource(name = "bCryptPasswordEncoder")
    private PasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private MessageSourceAccessor msa;
    @Autowired
    private InvitationMessageSender invitationMessageSender;

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

    public void checkSharedNoteBookOwner(User owner, InvitationDto invitationDto) {
        NoteBook sharedNotebook = noteBookService.getNoteBookById(invitationDto.getNotebookId());
        User sharedNotebookOwner = sharedNotebook.getOwner();
        if(invitationDto.getGuestIdList().contains(sharedNotebookOwner.getId())) {
            throw new AlreadyIncludeSharedNoteBookException(sharedNotebookOwner.getName());
        }
    }

    public void checkIncludePeer(InvitationDto invitationDto) {
        NoteBook sharedNotebook = noteBookService.getNoteBookById(invitationDto.getNotebookId());
        List<UserNameDto> includedPeers = sharedNotebook.getPeers().stream()
                .filter((peer) -> invitationDto.getGuestIdList().contains(peer.getId()))
                .map((peer) -> new UserNameDto(peer.getName()))
                .collect(Collectors.toList());
        if(!includedPeers.isEmpty()) {
            throw new AlreadyIncludeSharedNoteBookException(includedPeers);
        }
    }

    public void checkStatusIsPending(InvitationDto invitationDto) {
        List<Invitation> pendingInvitations = invitationService.getInvitationsByNoteBookId(invitationDto.getNotebookId());
        List<UserNameDto> pendingGuests = pendingInvitations.stream()
                .filter((invitation) -> invitation.getStatus() == InvitationStatus.PENDING)
                .filter((invitation) -> invitationDto.getGuestIdList().contains(invitation.getGuest().getId()))
                .map((invitation) -> new UserNameDto(invitation.getGuest().getName()))
                .collect(Collectors.toList());
        if(!pendingGuests.isEmpty()) {
            throw new AlreadyInviteGuestExcetion(pendingGuests);
        }
    }

    public void invite(User host, InvitationDto invitationDto) {
        checkSharedNoteBookOwner(host, invitationDto);
        checkIncludePeer(invitationDto);
        checkStatusIsPending(invitationDto);

        invitationDto.getGuestIdList().forEach((guestId) -> {
            saveInvitation(createInvitation(host.getId(), guestId, invitationDto.getNotebookId()));
        });
    }

    public Invitation createInvitation(Long hostId, Long guestId, Long notebookId) {
        User host = findUserById(hostId);
        User guest = findUserById(guestId);
        NoteBook noteBook = noteBookService.getNoteBookById(notebookId);
        return new Invitation(host, guest, noteBook);
    }

    public void saveInvitation(Invitation invitation) {
        invitationMessageSender.sendPersonalMessage(invitation.getGuest(), invitationService.save(invitation));
    }

    @Transactional
    public NoteBook addSharedNoteBook(User user, Long noteBookId) {
        User loginUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UnAuthorizedException(msa.getMessage("unauthorized.message")));
        NoteBook sharedNoteBook = noteBookService.getNoteBookById(noteBookId);
        sharedNoteBook.addPeer(loginUser);
        return sharedNoteBook;
    }

    public User findUserById(Long userId) {
        return this.userRepository.findById(userId).orElseThrow(() -> new RecordNotFoundException(msa.getMessage("NotFound.user")));
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RecordNotFoundException(msa.getMessage("NotFound.user")));
    }

    public List<InvitationGuestDto> searchEmailLike(String searchEmailText, User loginUser) {
        return this.userRepository.findByEmailLike("%" + searchEmailText + "%").stream().filter((user) -> !user.getEmail().equals(loginUser.getEmail()))
                .map((user) -> new InvitationGuestDto(user)).collect(Collectors.toList());
    }

    public List<NotificationMessageDto> getInvitations(User loginUser) {
        return this.invitationService.getInvitationsByGuestId(loginUser.getId())
                .stream()
                .filter((invitation) -> invitation.getStatus() == InvitationStatus.PENDING)
                .map((invitation) -> new NotificationMessageDto(invitation)).collect(Collectors.toList());
    }
}
