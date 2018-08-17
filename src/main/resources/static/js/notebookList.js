class NoteBookList {
    constructor() {
        this.noteBookList = $('.notebook-list');
        this.initNoteBookList();
    }

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
        return `<li data-notebook-id=${notebook.id}></li>
                <li>${notebook.title}</li>`;
    }

    getNoteBookFailCallback() {
        console.log("노트북 받아오기 실패")
    }
}

document.addEventListener("DOMContentLoaded", () => {
    new NoteBookList();
});