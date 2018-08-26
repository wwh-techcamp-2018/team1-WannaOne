function getNoteSectionTemplate(note) {
    return `<input id="note-section-note-title" data-note-id=${note.id} value="${note.title}"></input>
                <p id="note-section-meta">작성한 날짜: ${note.registerDatetime} &emsp; 수정한 날짜: ${note.updateDatetime}</p>`;
}

function getNoteBookListTemplate(notebook) {
    return `<li data-notebook-id=${notebook.id}>${notebook.title}</li>`;
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