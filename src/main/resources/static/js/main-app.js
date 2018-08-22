let mainApp;
let noteList;
let note;
let noteBookList;

class App {
    constructor() {
        noteBookList = new NoteBookList();
        noteList = new NoteList();
        note = new Note();
        this.initEventListeners();
    }

    initEventListeners() {
        $('#note-delete-button').addEventListener("click", () => this.removeCurrentNoteHandler());
    }

    focusDefaultNote() {
        if (noteList.isEmpty()) {
            note.empty();
            return;
        }

        let index = noteList.selectDefaultNote();
        let noteId = noteList.focus(index);
        note.getNote(noteId);
    }

    removeCurrentNoteHandler() {
        const removeSuccessCallback = noteList.removeCurrentNoteItem.bind(noteList);
        note.delete(removeSuccessCallback)
    }
}

document.addEventListener("DOMContentLoaded", () => {
    mainApp = new App();
});