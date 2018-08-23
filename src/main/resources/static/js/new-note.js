class NewNote {
    constructor() {
        this.noteSection = $('#note-section');
        this.editorEl = $('.te-ww-container .tui-editor-contents');
        this.noteSection = $('#note-section');
    }

    clearNoteSection() {
        this.noteSection.innerHTML = '';
    }

    updateNote(onSuccess, onFailure) {
        fetchManager({
            url: `/api/notes/${this.getNoteId()}`,
            method: 'PUT',
            headers: {'content-type': 'application/json'},
            body: JSON.stringify(
                {
                    title: this.getNoteTitle(),
                    text: this.getNoteText()
                }),
            onSuccess: onSuccess,
            onFailure: onFailure
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

    updateSuccessCallback(updatedNote) {
        this.clearNoteSection();
        this.renderNote(updatedNote);
    }
}