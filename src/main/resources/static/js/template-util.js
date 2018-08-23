function getNoteSectionTemplate(note, registerDatetime) {
    return `<input id="note-section-note-title" data-note-id=${note.id} value="${note.title}"></input>
                <p id="note-section-meta">${registerDatetime}</p>`;
}

function getNoteBookListTemplate(notebook) {
    return `<li data-notebook-id=${notebook.id}>${notebook.title}</li>`;
}

function getNoteItemTemplate(note, registerDatetime) {
    return `<li data-note-id="${note.id}">
                <div class="note-item">`
        + getNoteItemContentTemplate(note, registerDatetime)
        + `</div></li>`;
}

function getNoteItemContentTemplate(note, datetime) {
    return `<div class="note-list-title">${note.title}</div>
               <div class="note-list-snippet"><span>${datetime} </span>${note.text}</div>`;
}