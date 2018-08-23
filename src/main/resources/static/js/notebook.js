class NoteBook {
    constructor(noteBookListEl) {
        this.currentIndex = 0;
        this.noteBookTitleEl = $(".side-bar-middle-notebook-title");
        this.noteBookListEl = noteBookListEl;
    }

    initNoteBook(successCallBack, failCallBack) {
        fetchManager({
            url: '/api/notebooks',
            method: 'GET',
            onSuccess: successCallBack,
            onFailure: failCallBack
        });
    }

    initNoteBooks(noteBooks) {
        this.noteBooks = noteBooks;
        this.noteBooks.forEach((notebook) => {
            this.renderNoteBook(notebook);
        });

        this.noteBookListEl.firstElementChild.classList.add('notebook-focus');
    }
    getNotes() {
        return this.noteBooks[this.currentIndex].notes;
    }

    getNoteBookId() {
        return this.noteBooks[this.currentIndex].id;
    }

    setTitle() {
        this.noteBookTitleEl.innerHTML = this.noteBooks[this.currentIndex].title;
    }

    focusNoteBook(noteBookEl) {
        $('.notebook-focus').classList.toggle('notebook-focus');
        noteBookEl.classList.toggle('notebook-focus');
        this.currentIndex = getIndex(this.noteBookListEl.children, noteBookEl);
    }

    renderNoteBook(notebook) {
        this.noteBookListEl.insertAdjacentHTML('beforeend', getNoteBookListTemplate(notebook));
    }
}
