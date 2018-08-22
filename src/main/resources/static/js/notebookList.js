let noteBookList;
let noteList;

class NoteBookList {
    constructor() {
        this.noteBookList = [];
        this.noteBookListEl = $('.notebook-list');
        this.addNotebookInputButton = $('#add-notebook-input-btn');
        this.removeNotebookInputButton = $('#remove-notebook-input-btn');
        this.notebookInputWrapper = $('.notebook-input-wrapper');
        this.notebookTitleInput = $('#notebook-title-input');

        this.initNoteBookList();
        this.initEventListener();
    }

    // 노트북 목록 조회
    initNoteBookList() {
        this.noteBookListEl.addEventListener('click', this.changeNoteBookEventHandler.bind(this));
        fetchManager({
            url: '/api/notebooks',
            method: 'GET',
            onSuccess: this.getNoteBookSuccessCallback.bind(this),
            onFailure: this.getNoteBookFailCallback
        });
    }
    changeNoteBookEventHandler(e){
        const targetNotebook = e.target.closest('li');
        this.focusNoteBook(targetNotebook);
        const index = Array.prototype.indexOf.call(targetNotebook.parentElement.children, targetNotebook);
        noteList.renderNoteList(this.getNoteList(index));
    }

    getNoteBookSuccessCallback(notebookList) {
        this.noteBookList = notebookList;
        console.log(this.noteBookList);
        notebookList.forEach((notebook) => {
            this.renderNoteBook(notebook);
        });

        this.noteBookListEl.firstElementChild.classList.add('notebook-focus');
        noteList = new NoteList();
    }

    focusNoteBook(noteBookEl) {
        $('.notebook-focus').classList.toggle('notebook-focus');
        noteBookEl.classList.toggle('notebook-focus');
    }

    getNoteList(index) {
        return this.noteBookList[index].notes;
    }

    renderNoteBook(notebook) {
        this.noteBookListEl.insertAdjacentHTML('beforeend', getNoteBookListTemplate(notebook));
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

document.addEventListener("DOMContentLoaded", () => {
    noteBookList = new NoteBookList();
});