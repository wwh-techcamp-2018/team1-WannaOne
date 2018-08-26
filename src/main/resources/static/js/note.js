class Note {
    constructor() {
        this.noteSection = $('#note-section');
        this.editorEl = $('.te-ww-container .tui-editor-contents');
        this.editSection = $('#editSection');
        this.noteSection = $('#note-section');
        this.btns = $('.note-save-delete');
    }

    clearNoteSection() {
        this.noteSection.innerHTML = '';
        this.editSection.style.display = 'none';
        this.btns.style.display ='none';
    }

    updateNote(successCallback, failCallback) {
        fetchManager({
            url: `/api/notes/${this.getNoteId()}`,
            method: 'PUT',
            headers: {'content-type': 'application/json'},
            body: JSON.stringify(
                {
                    title: this.getNoteTitle(),
                    text: this.getNoteText()
                }),
            onSuccess: successCallback,
            onFailure: failCallback
        });
    }

    deleteNote(successCallback, failCallback) {
        fetchManager({
            url: `/api/notes/${this.getNoteId()}`,
            method: 'DELETE',
            onSuccess: successCallback,
            onFailure: failCallback
        });
    }

    renderNoteContent(data) {
        if(!data) {
            //TODO 선택될 노트가 없는 경우. 예외처리!!
            return;
        }
        this.clearNoteSection();
        this.renderNote(data);
    }

    renderNote(note) {
        this.note = note;
        this.noteSection.insertAdjacentHTML('beforeend', getNoteSectionTemplate(note));
        this.editorEl.innerHTML = note.text;
        this.editSection.style.display='block';
        this.btns.style.display='inline-block';
    }

    getNoteTitle() {
        return this.noteSection.firstElementChild.value;
    }

    getNoteText() {
        return this.editorEl.innerHTML;
    }

    getNoteId() {
        return this.note.id;
    }
}