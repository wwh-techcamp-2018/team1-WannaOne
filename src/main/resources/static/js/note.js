class Note {

    constructor() {
        this.noteSection = $('#note-section');
        this.initNotes();
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
                ${data.text}
                `;
    }

    clearNoteSection() {
        this.noteSection.innerHTML = '';
    }

    getNoteFailHandler() {
        console.log('노트 조회에 실패했습니다.');
    }
}

document.addEventListener("DOMContentLoaded", () => {
    new Note();
})