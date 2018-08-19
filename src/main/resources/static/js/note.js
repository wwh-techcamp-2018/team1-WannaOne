class Note {

    constructor() {
        this.noteSection = $('#note-section');
        this.noteListSection = $('.note-list');
        this.noteSaveButton = $('#note-save-button');
        this.initNotes();
        this.initButton();
    }

    initNotes() {
        fetchManager({
            url: '/api/notes/1',
            method: 'GET',
            onSuccess: this.getNoteSuccessCallback.bind(this),
            onFailure: this.getNoteFailHandler
        })
    }

    getNoteSuccessCallback(data) {
        this.clearNoteSection();
        this.renderNote(data);
    }

    renderNote(data) {
        console.log(data);
        this.noteSection.insertAdjacentHTML('beforeend', this.noteSectionFormatter(data));
    }

    noteSectionFormatter(data) {
        return this.noteSectionTemplate(data, datetimeFormatter(data.registerDatetime));
    }

    noteSectionTemplate(data, registerDatetime) {
        return `<h1 id="note-section-note-title">${data.title}</h1>
                <p id="note-section-meta">${registerDatetime}</p>
                <textarea id="note-section-note-text" rows="15" cols="50">
                ${data.text}
                </textarea>
                `;
    }

    appendNoteItem(data) {
        this.noteListSection.insertAdjacentHTML('beforeend', this.getNoteTemplate(data));
    }

    getNoteTemplate(note) {
        return ` <li data-note-id="${note.id}">
            <div class="note-item">
                <p class="note-list-title">${note.title}</p>
            <p class="note-list-snippet">${note.text}</p>
            </div>
            </li>`
    }

    clearNoteSection() {
        this.noteSection.innerHTML = '';
    }

    getNoteFailHandler() {
        console.log('노트 조회에 실패했습니다.');
    }

    initButton() {
        this.noteSaveButton.addEventListener("click", this.saveHandler.bind(this));
    }
    saveHandler() {
        const title = $("#note-section-note-title").innerText;
        //TODO: if new post, it should be update time.
        const text = $("#note-section-note-text").value;
        fetchManager({
                    url: '/api/notes',
                    method: 'POST',
                    headers: {'content-type': 'application/json'},
                    body: JSON.stringify({title: title,
                            text: text,
                            registerDatetime: Date.now(), //TODO: change to NoteDTO. to delete registerDate here.
                            updateDatetime: Date.now()}),
                    onSuccess: this.postNoteSuccessCallback.bind(this),
                    onFailure: this.postNoteFailHandler
                })
    }

    postNoteSuccessCallback(data){
        this.appendNoteItem(data);
    }

    postNoteFailHandler() {
        console.log('노트 작성에 실패했습니다.');
    }
}

document.addEventListener("DOMContentLoaded", () => {
    new Note();
})