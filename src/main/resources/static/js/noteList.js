class NoteList {
    constructor() {
        this.noteListSection = $('.note-list');
        this.initNoteList();
    }

    initNoteList() {
        this.noteListSection.addEventListener("click", this.getNote.bind(this));
        fetchManager({
            url: '/api/notes/all',
            method: 'GET',
            onSuccess: this.getNoteListSuccessCallback.bind(this),
            onFailure: this.getNoteListFailHandler
        })
    }

    getNote(e) {
        const liElement = e.target.closest('li');
        if (this.isNewItemClicked(liElement)) {
            const noteId = liElement.dataset.noteId;
            note.getNote(noteId);
            $('.note-item-focus').classList.toggle('note-item-focus');
            liElement.firstElementChild.classList.toggle('note-item-focus');
        }
    }

    getNoteListSuccessCallback(data) {
        this.clearNoteListSection();
        this.renderNoteList(data);
    }

    renderNoteList(data) {
        console.log(data);
        data.forEach((note) => this.renderNoteItem(note));
        this.focusFirstNoteItem();
    }

    renderNoteItem(note) {
        this.noteListSection.insertAdjacentHTML('beforeend', this.noteItemFormatter(note));
    }

    noteItemFormatter(note) {
        return this.noteItemTemplate(note, dateFormatter(note.registerDatetime));
    }

    noteItemTemplate(note, registerDatetime) {
       return `<li data-note-id="${note.id}">
            <div class="note-item">
                <div class="note-list-title">${note.title}</div>
            <div class="note-list-snippet"><span>${registerDatetime} </span>${note.text}</div>
            </div>
            </li>`
    }

    isNewItemClicked(liElement) {
        return $('.note-item-focus') !== liElement.firstElementChild;
    }

    clearNoteListSection() {
        this.noteListSection.innerHTML = '';
    }

    focusFirstNoteItem() {
        this.noteListSection.firstElementChild.firstElementChild.classList.add('note-item-focus');
    }

    getNoteListFailHandler() {
        console.log('노트 목록 조회에 실패했습니다.');
    }

}

document.addEventListener("DOMContentLoaded", () => {
    new NoteList();
})