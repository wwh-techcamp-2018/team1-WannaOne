class NoteBookList {
    constructor() {
        this.noteBookList = $('.sidebar-elements');
        this.addNotebookTitleInputButton = $('#add-notebook-title-input-btn');
        this.removeNotebookTitleInputButton = $('#remove-notebook-title-input-btn');
        this.notebookTitleInputWrapper = $('.notebook-title-input-wrapper');
        this.notebookTitleInput = $('#notebook-title-input');

        this.addNotebookTitleInputButton.addEventListener('click', this.handlerAddNoteBookTitleInputEvent.bind(this));
        this.removeNotebookTitleInputButton.addEventListener('click', this.handlerRemoveNoteBookTitleInputEvent.bind(this));
        this.notebookTitleInput.addEventListener('keyup', this.handlerAddNoteBookEvent.bind(this));
    }

    handlerAddNoteBookTitleInputEvent(evt) {
        this.notebookTitleInputWrapper.style.display = "block";
    }

    handlerRemoveNoteBookTitleInputEvent(evt) {
        this.notebookTitleInputWrapper.style.display = "none";
    }

    handlerAddNoteBookEvent(evt) {
        if(evt.keyCode === 13) {
            const notebookTitle = this.notebookTitleInput.value;
            if(notebookTitle === '') return;
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

    addNoteBook(notebook) {
        this.noteBookList.insertAdjacentHTML('beforeend', this.noteBookListTemplate(notebook));
    }

    noteBookListTemplate(notebook) {
        return `<li data-notebook-id=${notebook.id}></li>
                <li class="parent"><a href="#"><i class="icon mdi mdi-face"></i><span>${notebook.title}</span></a></li>`;
    }

    clearInput() {
        this.notebookTitleInput.value = '';
    }

    addNoteBookFailureCallback() {
        console.log("노트북 생성 실패")
    }
}

document.addEventListener("DOMContentLoaded", () => {
    new NoteBookList();
});