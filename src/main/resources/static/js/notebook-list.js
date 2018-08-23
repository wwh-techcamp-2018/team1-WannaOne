class NotebookList {
    constructor(noteBookListEl) {
        this.currentIndex = 0;
        this.notebookTitleEl = $(".side-bar-middle-notebook-title");
        this.notebookListEl = noteBookListEl;

        this.notebookTitleInput = $('#notebook-title-input');
        this.notebookInputWrapper = $('.notebook-input-wrapper');
        this.addNotebookInputButton = $('#add-notebook-input-btn');
        this.removeNotebookInputButton = $('#remove-notebook-input-btn');

    }

    initNotebookList(successCallback, failCallback) {
        this.fetchNotebookList(successCallback, failCallback);
        this.addNotebookInputButton.addEventListener('click', () => this.notebookInputWrapper.style.display = "block");
        this.removeNotebookInputButton.addEventListener('click', () => this.notebookInputWrapper.style.display = "none");
        this.notebookTitleInput.addEventListener('keyup', this.handlerAddNoteBookEvent.bind(this));
    }

    fetchNotebookList(successCallback, failCallback) {
        fetchManager({
            url: '/api/notebooks',
            method: 'GET',
            onSuccess: successCallback,
            onFailure: failCallback
        });
    }

    renderNotebooks(noteBooks) {
        this.noteBooks = noteBooks;
        this.noteBooks.forEach((notebook) => {
            this.renderNoteBook(notebook);
        });

        this.notebookListEl.firstElementChild.classList.add('notebook-focus');
    }
    getNotes() {
        if(!this.noteBooks) {
            //TODO 삭제해서 0이 되는 경우도 체크해야함. 아래 메소드도.
            console.log('선택된 노트북이 없습니다.');
            return;
        }
        return this.noteBooks[this.currentIndex].notes;
    }

    getNoteBookId() {
        if(!this.noteBooks) {
            console.log('선택된 노트북이 없습니다.');
            return;
        }
        return this.noteBooks[this.currentIndex].id;
    }

    setTitle() {
        this.notebookTitleEl.firstElementChild.innerHTML = this.noteBooks[this.currentIndex].title;
    }

    focusNoteBook(noteBookEl) {
        $('.notebook-focus').classList.toggle('notebook-focus');
        noteBookEl.classList.toggle('notebook-focus');
        this.currentIndex = getIndex(this.notebookListEl.children, noteBookEl);
    }

    renderNoteBook(notebook) {
        this.notebookListEl.insertAdjacentHTML('beforeend', getNoteBookListTemplate(notebook));
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
