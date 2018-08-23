function getNoteSectionTemplate(data) {
    return `<input id="note-section-note-title" data-note-id=${data.id} value="${data.title}"></input>
                <p id="note-section-meta">${data.registerDatetime}</p>`;
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
    return `<div class="note-list-title">${note.title}</div>
               <div class="note-list-snippet"><span>${note.updateDatetime} </span>${note.text}</div>`;
}