class NoteList {
    constructor() {
        this.noteListSection = $('.note-list');
        this.initNoteList();
    }

    initNoteList() {
        this.noteListSection.addEventListener("click", this.getNote.bind(this));
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

    renderNoteList(data) {
        this.clearNoteListSection();
        data.forEach((note) => this.renderNoteItem(note));
        this.focusFirstNoteItem();
    }

    renderNoteItem(note) {
        this.noteListSection.insertAdjacentHTML('beforeend', this.noteItemFormatter(note));
    }

    noteItemFormatter(note) {
        return getNoteItemTemplate(note, dateFormatter(note.registerDatetime));
    }

    updateNoteItem(note) {
        const noteItem = $('.note-item-focus')
        noteItem.innerHTML = this.noteItemUpdateFormatter(note);
    }

    noteItemUpdateFormatter(note) {
        return getNoteItemContentTemplate(note, dateFormatter(note.updateDatetime));
    }

    isNewItemClicked(liElement) {
        return $('.note-item-focus') !== liElement.firstElementChild;
    }

    clearNoteListSection() {
        this.noteListSection.innerHTML = '';
    }

    focusFirstNoteItem() {
        if (this.noteListSection.children.length > 0) {
            this.noteListSection.firstElementChild.firstElementChild.classList.add('note-item-focus');
        } else {
            //TODO: 빈 화면 처리 나중에 해주어야 함.
        }
    }

    removeCurrentNoteItem() {
        const noteItem = $('.note-item-focus');
        this.noteListSection.removeChild(noteItem.closest('li'));
        mainApp.focusDefaultNote();
    }

    selectDefaultNote() {
        this.focusFirstNoteItem()
    }

    focus(index) {
        $('.note-item-focus').classList.toggle('note-item-focus');
        const currentFocusNoteItem = $('.note-item-focus');
        if (currentFocusNoteItem) {
            currentFocusNoteItem.classList.toggle('note-item-focus');
        }
        this.noteListSection.children[index].classList.toggle('note-item-focus');
        return $('.note-item-focus').closest('li').dataset.noteId;
    }

    isEmpty() {
        return this.noteListSection.childElementCount <= 0;
    }
}