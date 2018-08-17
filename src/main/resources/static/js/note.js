class Note {

    constructor() {
        this.noteSection = $('#note-section');
        this.editor = $('.te-ww-container .tui-editor-contents')
        this.noteSaveButton = $('#note-save-button');
        this.initNotes();
        this.initButton();
    }

    initNotes() {
        fetchManager({
            url: '/api/notes/11',
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
        this.editor.insertAdjacentHTML('beforeend', data.text);

    }

    noteSectionFormatter(data) {
        return this.noteSectionTemplate(data, datetimeFormatter(data.registerDatetime));
    }

    noteSectionTemplate(data, registerDatetime) {
        return `<textarea id="note-section-note-title" data-note-id=${data.id}>${data.title}</textarea>
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
                            registerDatetime: 1534219200000, //TODO: change to NoteDTO. to delete registerDate here.
                            updateDatetime: Date.now()}),
                    onSuccess: this.postNoteSuccessCallback,
                    onFailure: this.postNoteFailHandler
                })
    }

    postNoteSuccessCallback(response){
        console.log('노트 작성에 성공했습니다. 노트번호: ', response);
    }

    postNoteFailHandler() {
        console.log('노트 작성에 실패했습니다.');
    }
}

document.addEventListener("DOMContentLoaded", () => {
    new Note();
})