function getNoteSectionTemplate(note) {
    return `<input id="note-section-note-title" data-note-id=${note.id} value="${note.title}"></input>
                <p id="note-section-meta">작성한 날짜: ${note.registerDatetime} &emsp; 수정한 날짜: ${note.updateDatetime}</p>`;
}

//function getCommentListTemplate(list) {
//    let result = `댓글 총 ${list.length}개`;
//
//    `<ul class="comment-list">`;
//    list.forEach(comment => result += getCommentTemplate(comment));
//    result = `댓글 총 ${list.length}개` + result + `</ul>`;
//    return result;
//}

function getCommentTemplate(comment) {
    let result = `<li data-comment-id=${comment.id}>
                    <table class="comment-show-section">
                        <tr>
                            <td width="5%" rowspan="2" class="comment-writer-img">
                                <img src="https://scontent-icn1-1.xx.fbcdn.net/v/t1.0-1/p80x80/14358870_804012509736148_3098132645899318746_n.jpg?_nc_cat=0&oh=76ae40d5e3cf9954708a842bc2caa5ad&oe=5C378074">
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
               <div class="note-list-snippet"><span>${note.updateDatetime} </span>${note.text}</div>`;
}

function getSharedNoteBookHeader() {
    return `<hr><div class="shared-notebook-header">나의 공유노트북</div>`;
}

function getInvitedGuestItemTemplate(invitedGuest) {
    return `<li data-guestId="${invitedGuest.id}">${invitedGuest.name}<i class="material-icons invitation-cancel-button">close</i></li>`;
}

function getNotificationItem(notification) {
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
