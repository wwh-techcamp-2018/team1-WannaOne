class NoteBookList {
    constructor() {
        this.noteBookList = $('.notebook-list');
        this.addNotebookInputButton = $('#add-notebook-input-btn');
        this.removeNotebookInputButton = $('#remove-notebook-input-btn');
        this.notebookInputWrapper = $('.notebook-input-wrapper');
        this.notebookTitleInput = $('#notebook-title-input');

        this.initNoteBookList();
        this.initEventListener();
    }

    // 노트북 목록 조회
    initNoteBookList() {
        fetchManager({
            url: '/api/notebooks',
            method: 'GET',
            onSuccess: this.getNoteBookSuccessCallback.bind(this),
            onFailure: this.getNoteBookFailCallback
        })
    }

    getNoteBookSuccessCallback(notebookList) {
        notebookList.forEach((notebook) => {
            this.addNoteBook(notebook);
        });
    }

    addNoteBook(notebook) {
        this.noteBookList.insertAdjacentHTML('beforeend', this.noteBookListTemplate(notebook));
    }

    noteBookListTemplate(notebook) {
        return `<li data-notebook-id=${notebook.id}>${notebook.title}</li>`;
    }

    getNoteBookFailCallback() {
        console.log("노트북 받아오기 실패")
    }

    // 노트북 추가
    initEventListener() {
        this.addNotebookInputButton.addEventListener('click', this.handlerAddNoteBookInputEvent.bind(this));
        this.removeNotebookInputButton.addEventListener('click', this.handlerRemoveNoteBookInputEvent.bind(this));
        this.notebookTitleInput.addEventListener('keyup', this.handlerAddNoteBookEvent.bind(this));
    }

    handlerAddNoteBookInputEvent(evt) {
        this.notebookInputWrapper.style.display = "block";
    }

    handlerRemoveNoteBookInputEvent(evt) {
        this.notebookInputWrapper.style.display = "none";
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
        this.addNoteBook(notebook);
        this.clearInput();
    }

    clearInput() {
        this.notebookTitleInput.value = '';
    }

    addNoteBookFailureCallback() {
        console.log("노트북 추가 실패")
    }
}

document.addEventListener("DOMContentLoaded", () => {
    new NoteBookList();
});