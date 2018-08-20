let note;

class Note {

    constructor() {
        this.noteSection = $('#note-section');
        this.editor = $('.te-ww-container .tui-editor-contents')
        this.noteListSection = $('.note-list');
        this.noteSaveButton = $('#note-save-button');
        this.initNote();
        this.initButton();
    }

    initNote() {
        this.getNote(1);
    }

    getNote(noteId) {
        fetchManager({
            url: `/api/notes/${noteId}`,
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
        this.noteSection.insertAdjacentHTML('beforeend', this.noteSectionFormatter(data));
        this.editor.innerHTML = data.text;
    }

    noteSectionFormatter(data) {
        return this.noteSectionTemplate(data, datetimeFormatter(data.registerDatetime));
    }

    noteSectionTemplate(data, registerDatetime) {
        return `<input id="note-section-note-title" data-note-id=${data.id} value="${data.title}"></input>
                <p id="note-section-meta">${registerDatetime}</p>`;
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
        const title = $("#note-section-note-title").value;
        //TODO: if new post, it should be update time.
        const text = this.editor.innerHTML;
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
        noteList.renderNoteItem(data);
    }

    postNoteFailHandler() {
        console.log('노트 작성에 실패했습니다.');
    }
}

document.addEventListener("DOMContentLoaded", () => {
    note = new Note();
})