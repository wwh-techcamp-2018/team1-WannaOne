class NotebookList {
    constructor(noteBookListEl) {
        this.currentIndex = 0;
        this.noteBookTitleEl = $(".side-bar-middle-notebook-title");
        this.noteBookListEl = noteBookListEl;

        this.notebookTitleInput = $('#notebook-title-input');
        this.notebookInputWrapper = $('.notebook-input-wrapper');
        this.addNotebookInputButton = $('#add-notebook-input-btn');
        this.removeNotebookInputButton = $('#remove-notebook-input-btn');

    }

    initNoteBook(successCallBack, failCallBack) {
        fetchManager({
            url: '/api/notebooks',
            method: 'GET',
            onSuccess: successCallBack,
            onFailure: failCallBack
        });

        this.addNotebookInputButton.addEventListener('click', () => this.notebookInputWrapper.style.display = "block");
        this.removeNotebookInputButton.addEventListener('click', () => this.notebookInputWrapper.style.display = "none");
        this.notebookTitleInput.addEventListener('keyup', this.handlerAddNoteBookEvent.bind(this));


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
        this.noteBookTitleEl.firstElementChild.innerHTML = this.noteBooks[this.currentIndex].title;
    }

    focusNoteBook(noteBookEl) {
        $('.notebook-focus').classList.toggle('notebook-focus');
        noteBookEl.classList.toggle('notebook-focus');
        this.currentIndex = getIndex(this.noteBookListEl.children, noteBookEl);
    }

    renderNoteBook(notebook) {
        this.noteBookListEl.insertAdjacentHTML('beforeend', getNoteBookListTemplate(notebook));
    }

    handlerAddNoteBookEvent(evt) {
        if(evt.keyCode === 13) {
            const notebookTitle = this.notebookTitleInput.value;
            if(!notebookTitle) return;
            fetchManager({
                url: '/api/notebooks',
                method: 'POST',
                headers: {'content-type': 'application/json'},
                body: JSON.stringify({
                    title: notebookTitle
                }),
                onSuccess: this.addNoteBookSuccessCallback.bind(this),
                onFailure: this.addNoteBookFailureCallback
            });
        }
    }

    addNoteBookSuccessCallback(notebook) {
        this.renderNoteBook(notebook);
        this.clearInput();
    }

    clearInput() {
        this.notebookTitleInput.value = '';
    }

    addNoteBookFailureCallback() {
        console.log("노트북 추가 실패")
    }

}
