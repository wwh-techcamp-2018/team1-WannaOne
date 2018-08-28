function getNoteSectionTemplate(note) {
    return `<input id="note-section-note-title" data-note-id=${note.id} value="${note.title}"></input>
                <p id="note-section-meta">작성한 날짜: ${note.registerDatetime} &emsp; 수정한 날짜: ${note.updateDatetime}</p>`;
}

function getCommentListTemplate(list) {
    let result = `<ul class='comment-list'>`;
    list.forEach(comment => result += getCommentTemplate(comment));
    result = `댓글 총 ${list.length}개` + result + `</ul>`;
    return result;
}

function getCommentTemplate(comment) {
    return `<li>${comment.content}</li>`;
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