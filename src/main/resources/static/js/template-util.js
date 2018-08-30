function getNoteSectionTemplate(note) {
    return `<input id="note-section-note-title" data-note-id=${note.id} value="${note.title}" autocomplete="off"></input>
               <div class="note-section-meta">
                    <div id="note-section-meta-writer"><img src="${note.writer.photoUrl}"><span>${note.writer.name}</span></div>
                    <div id="note-section-meta-datetime"><span>작성한 날짜: ${note.registerDatetime} &emsp; 수정한 날짜: ${note.updateDatetime}</span></div>
                </div>`;
}

function getCommentTemplate(comment) {
    let result = `<li data-comment-id=${comment.id} data-is-writer=${comment.isWriter}>
                    <table class="comment-show-section">
                        <tr>
                            <td width="5%" rowspan="2" class="comment-writer-img">
                                <img src="${comment.writer.photoUrl}">
                            </td>
                            <td width="90%" class="comment-writer">${comment.writer.name}</td>`
    result += getCommentDeleteTemplate(comment);
    result += `</tr>
               <tr>
                   <td class="comment-content">${comment.content}</td>
               </tr>
           </table>
       </li>`;
    return result;
}

function getCommentDeleteTemplate(comment) {
    let html = '';
    if(comment.isWriter) {
         html += `<td width="5%" class="delete-comment-button"><i class="far fa-trash-alt"></i></td>`;
    }
    return html;
}

function getNoteBookListTemplate(notebook) {
    return `<li data-notebook-id=${notebook.id}>
                <span>${notebook.title}</span>
                <i class="far fa-trash-alt"></i>
            </li>`;
}

function getSharedNoteBookTemplate(sharedNotebook, user) {
    let html =  `<li data-notebook-id=${sharedNotebook.id}>
                    <span>${sharedNotebook.title}</span>
                    <i class="fas fa-share-alt"></i>`;
    if(sharedNotebook.owner.id === user.id) {
        html += `<i class="far fa-trash-alt"></i>
                </li>`;
    }
    return html;
}

function getNoteItemTemplate(note) {
    return `<li data-note-id="${note.id}">
                <div class="note-item">`
        + getNoteItemContentTemplate(note)
        + `</div></li>`;
}

function getNoteItemContentTemplate(note) {
    return `<div class="note-list-title" data-note-id="${note.id}" draggable="true">${note.title}</div>
               <div class="note-list-snippet">
               <span class="note-list-datetime">${note.updateDatetime} </span>
               <span class="note-list-snippet-text">${note.text}</span>
                    </div>`;
}

function getSharedNoteBookHeader() {
    return `<hr><div class="shared-notebook-header">나의 공유노트북</div>`;
}

function getInvitedGuestItemTemplate(invitedGuest) {
    return `<li data-guestId="${invitedGuest.id}" class="invitation-list-item">${invitedGuest.name}<i class="material-icons invitation-cancel-button">close</i></li>`;
}

function getInvitationNotificationItem(notification) {
    return `<li class="invitation-request" data-invitation-id="${notification.id}">${notification.message}
                <button class="invitation-accept-button" type="button">수락</button>
                <button class="invitation-reject-button" type="button">거절</button>
            </li>`;
}

function getNotificationNumber(number) {
    if (number == 0) {
        return ``;
    }
    return `<span class="button__badge">${number}</span>`
}


function getAutoCompleteListItem(autoCompleteUser) {
    return `<li data-user-id="${autoCompleteUser.id}" data-user-name="${autoCompleteUser.name}">${autoCompleteUser.email}</li>`;
}

function getInviteUserItem(userId, email) {
    return `<li data-user-id="${userId}">${email}</li>`
}

function getNotificationItem(notification) {
    return `<li class="invitation-request" data-invitation-id="${notification.id}">${notification.message}
            </li>`;
}