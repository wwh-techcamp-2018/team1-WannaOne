class NoteList {
    constructor() {
        this.noteListSection = $('.note-list ul');
        this.initNoteList();
    }

    initNoteList() {
        fetchManager({
            url: '/api/notes/all',
            method: 'GET',
            onSuccess: this.getNoteListSuccessCallback.bind(this),
            onFailure: this.getNoteListFailHandler
        })
    }

    getNoteListSuccessCallback(data) {
        this.clearNoteListSection();
        this.renderNoteList(data);
    }

    renderNoteList(data) {
        console.log(data);

        data.forEach((note) => this.renderNoteItem(note));
    }

    renderNoteItem(note) {
        this.noteListSection.insertAdjacentHTML('beforeend', this.noteItemFormatter(note));
    }

    noteItemFormatter(note) {
        return this.noteItemTemplate(note);
    }

    noteItemTemplate(note) {
       return ` <li data-note-id="${note.id}">
            <div class="note-item">
                <p class="note-list-title">${note.title}</p>
            <p class="note-list-snippet">${note.text}</p>
            </div>
            </li>`
    }

    clearNoteListSection() {
        this.noteListSection.innerHTML = '';
    }

    getNoteListFailHandler() {
        console.log('노트 목록 조회에 실패했습니다.');
    }

}

document.addEventListener("DOMContentLoaded", () => {
    new NoteList();
})