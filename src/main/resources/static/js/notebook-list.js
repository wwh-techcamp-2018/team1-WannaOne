class NotebookList {
    constructor(noteBookListEl) {
        this.currentIndex = 0;
        this.notebookTitleEl = $(".side-bar-middle-notebook-title");
        this.notebookListEl = noteBookListEl;
        this.sharedNoteBookButton = $('.share-notebook-open-button');
        this.sharedInfoSection = $('.shared-info');
        this.sharedInfoCountEl = $('.shared-count');

        this.hideNoteListButton = $('#hide-note-list-btn');
        this.notebookTitleInput = $('#notebook-title-input');
        this.notebookInputWrapper = $('.notebook-input-wrapper');
        this.addNotebookInputButton = $('#add-notebook-input-btn');
        this.addNotebookInputButton.addEventListener('click', () => {
            const display = this.notebookInputWrapper.style.display;
            if(display === 'block') {
                this.notebookInputWrapper.style.display = 'none';
                return;
            }
            this.notebookInputWrapper.style.display = 'block';
        });
        document.addEventListener('click', (e) => this.closeNotebookInputHandler(e));
    }

    setOwner(owner) {
        this.owner = owner;
    }

    clearNoteBookList() {
        this.notebookListEl.innerHTML = '';
    }

    toggleHideNoteListButton() {
        this.hideNoteListButton.classList.toggle('fa-angle-left');
        this.hideNoteListButton.classList.toggle('fa-angle-right');
    }

    fetchNotebookList(successCallback, failCallback) {
        fetchManager({
            url: '/api/notebooks/all',
            method: 'GET',
            onSuccess: successCallback,
            onFailure: failCallback
        });
    }

    fetchDeleteNoteBook(noteBookId, successCallback, failCallback) {
        fetchManager({
            url: `/api/notebooks/${noteBookId}`,
            method: 'DELETE',
            onSuccess: successCallback,
            onFailure: failCallback
        });
    }

    renderNotebooks(noteBooks) {
        this.noteBooks = noteBooks;
        this.currentIndex = 0;
        let firstSharedNoteBook = true;
        this.noteBooks.forEach((notebook) => {
            if(notebook.peers.length > 0 && firstSharedNoteBook) {
                this.renderSharedNoteBookHeader();
                firstSharedNoteBook = false;
            }
            this.renderNoteBook(notebook);
        });

        this.notebookListEl.firstElementChild.classList.add('notebook-focus');
    }

    deleteNoteBook(deleteTarget, successCallback, failCallback) {
        const noteBookId = deleteTarget.closest('LI').dataset.notebookId;
        this.fetchDeleteNoteBook(noteBookId, successCallback, failCallback);
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
        const selectedNoteBook = this.noteBooks[this.currentIndex];
        this.notebookTitleEl.innerText = selectedNoteBook.title;
        this.notebookTitleEl.dataset.notebookId = selectedNoteBook.id;
        const peersCount = selectedNoteBook.peers.length;
        if(peersCount > 0) {
            this.sharedInfoSection.style.display = 'block';
            this.sharedInfoCountEl.innerHTML = peersCount + 1;
            return;
        }
        this.sharedInfoSection.style.display = 'none';
    }

    focusNoteBook(noteBookEl) {
        $('.notebook-focus').classList.toggle('notebook-focus');
        noteBookEl.classList.toggle('notebook-focus');
        this.currentIndex = getIndex($All('.notebook-list > li'), noteBookEl);
    }

    addNoteBook(notebook) {
        const index = this.getMyNoteBookLastIndex() - 1;
        const targetElement = $All('.notebook-list > li')[index];
        if(index < 0) {
            this.notebookListEl.insertAdjacentHTML('afterbegin', getNoteBookListTemplate(notebook));
            return;
        }
        targetElement.insertAdjacentHTML('afterend', getNoteBookListTemplate(notebook));
    }

    getMyNoteBookLastIndex() {
        for(let i = 0; i<this.noteBooks.length; i++) {
            if(this.noteBooks[i].peers.length > 0) {
                return i - 1;
            }
        }
        return 0;
    }

    renderNoteBook(notebook) {
        if(!notebook.peers.length > 0) {
            this.notebookListEl.insertAdjacentHTML('beforeend', getNoteBookListTemplate(notebook));
            return;
        }

        this.notebookListEl.insertAdjacentHTML('beforeend', getSharedNoteBookTemplate(notebook, this.owner));
    }

    renderSharedNoteBookHeader() {
        this.notebookListEl.insertAdjacentHTML('beforeend', getSharedNoteBookHeader());
    }

    createNewNotebook(successCallback, failCallback) {
        const notebookTitle = this.notebookTitleInput.value;
        if(!notebookTitle) return;
        fetchManager({
            url: '/api/notebooks',
            method: 'POST',
            headers: {'content-type': 'application/json'},
            body: JSON.stringify({
                title: replaceTagBrackets(notebookTitle)
            }),
            onSuccess: successCallback,
            onFailure: failCallback
        });
    }

    addNoteBookSuccessCallback(notebook) {
        debugger;
        console.log(notebook);
        this.noteBooks.splice(this.getMyNoteBookLastIndex() + 1, 0, notebook);
        this.addNoteBook(notebook);
        this.clearInput();
    }

    clearInput() {
        this.notebookTitleInput.value = '';
        this.notebookInputWrapper.style.display = "none";
    }

    addNoteBookFailureCallback(error) {
        error.json().then((error) => {
            error.errors.forEach((error) => {
                const validationEl = $(`#notebook-${error.fieldName}-validation`);
                validationEl.style.display = 'inline-block';
                validationEl.innerHTML = error.errorMessage;
                setTimeout(() => {
                    validationEl.style.display = 'none';
                }, 2000);
            })
        });
        console.log("노트북 추가 실패")
    }

    closeNotebookInputHandler(e) {
        if (this.addNotebookInputButton.contains(e.target) || this.notebookTitleInput.contains(e.target)) {
            return;
        }
        this.notebookInputWrapper.style.display = 'none';
    }

}
