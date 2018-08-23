class Note {
    constructor() {
        this.noteSection = $('#note-section');
        this.editor = $('.te-ww-container .tui-editor-contents')
        this.noteListSection = $('.note-list');
        this.noteSaveButton = $('#note-save-button');
        this.noteDeleteButton = $('#note-delete-button');
        this.initNote();
        this.initButton();
    }

    initNote() {
        this.getNote(1);
    }

    getNote(noteId) {
        fetchManager({
            url: `/api/notes/${noteId}`,
            method: 'GET',
            onSuccess: this.getNoteSuccessCallback.bind(this),
            onFailure: this.getNoteFailHandler
        })
    }

    getNoteSuccessCallback(note) {
        this.clearNoteSection();
        this.renderNote(note);
    }

    renderNote(note) {
        this.noteSection.insertAdjacentHTML('beforeend', this.noteSectionFormatter(note));
        this.editor.innerHTML = note.text;
        this.noteId = note.id;
    }

    noteSectionFormatter(note) {
        return getNoteSectionTemplate(note, datetimeFormatter(note.registerDatetime));
    }

    clearNoteSection() {
        this.noteSection.innerHTML = '';
    }

    getNoteFailHandler() {
        console.log('노트 조회에 실패했습니다.');
    }

    initButton() {
        this.noteSaveButton.addEventListener("click", () => this.updateHandler());
        this.noteDeleteButton.addEventListener("click", () => this.deleteHandler());
    }

    // 노트 수정
    updateHandler() {
        const title = $("#note-section-note-title").value;
        const text = this.editor.innerHTML;
        fetchManager({
                    url: `/api/notes/${this.noteId}`,
                    method: 'PUT',
                    headers: {'content-type': 'application/json'},
                    body: JSON.stringify(
                        {
                            title: title,
                            text: text
                        }),
                    onSuccess: this.noteUpdateSuccessCallback.bind(this),
                    onFailure: this.noteUpdateFailHandler
                })
    }

    noteUpdateSuccessCallback(note) {
        noteList.updateNoteItem(note);
    }

    noteUpdateFailHandler() {
        console.log('노트 수정에 실패했습니다.');
    }

    deleteHandler() {
        fetchManager({
            url: `/api/notes/${this.noteId}`,
            method: 'DELETE',
            onSuccess: this.noteDeleteSuccessCallback.bind(this),
            onFailure: this.noteDeleteFailHandler
        })
    }

    noteDeleteSuccessCallback() {
        //TODO 1) 노트 리스트에 노트가 남아있다면 삭제한 노트의 다음 노트 선택
        //TODO 2) 노트가 남아있지 않다면 빈화면
        console.log('노트 삭제 성공');
    }

    noteDeleteFailHandler() {
        console.log('노트 삭제 실패');
    }
}