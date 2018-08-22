class Note {
    constructor() {
        this.noteSection = $('#note-section');
        this.editor = $('.te-ww-container .tui-editor-contents')
        this.noteListSection = $('.note-list');
        this.noteSaveButton = $('#note-save-button');
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

    getNoteSuccessCallback(data) {
        this.clearNoteSection();
        this.renderNote(data);
    }

    renderNote(data) {
        this.noteSection.insertAdjacentHTML('beforeend', this.noteSectionFormatter(data));
        this.editor.innerHTML = data.text;
    }

    noteSectionFormatter(data) {
        return getNoteSectionTemplate(data, datetimeFormatter(data.registerDatetime));
    }

    clearNoteSection() {
        this.noteSection.innerHTML = '';
    }

    getNoteFailHandler() {
        console.log('노트 조회에 실패했습니다.');
    }

    initButton() {
//        this.noteSaveButton.addEventListener("click", this.saveHandler.bind(this));
        this.noteSaveButton.addEventListener("click", () => this.updateHandler());
    }

    // 노트 수정
    updateHandler() {
        const noteId = $("#note-section-note-title").dataset.noteId;
        const title = $("#note-section-note-title").value;
        const text = this.editor.innerHTML;
        fetchManager({
                    url: `/api/notes/${noteId}`,
                    method: 'PUT',
                    headers: {'content-type': 'application/json'},
                    body: JSON.stringify(
                        {
                            title: title,
                            text: text
                        }),
                    onSuccess: this.noteUpdateSuccessCallback.bind(this),
                    onFailure: this.NoteUpdateFailHandler
                })
    }

    noteUpdateSuccessCallback(data) {
        noteList.updateNoteItem(data);
    }

    NoteUpdateFailHandler() {
        console.log('노트 수정에 실패했습니다.');
    }

    // 새 노트 저장
    saveHandler() {
        const title = $("#note-section-note-title").value;
        //TODO: if new post, it should be update time.
        const text = this.editor.innerHTML;
        const noteBookId = $('.notebook-focus').dataset.notebookId;
        fetchManager({
                    url: `/api/notes/notebook/${noteBookId}`,
                    method: 'POST',
                    headers: {'content-type': 'application/json'},
                    body: JSON.stringify({
                            title: title,
                            text: text
                    }),


                    onSuccess: this.postNoteSuccessCallback.bind(this),
                    onFailure: this.postNoteFailHandler
                })
    }

    postNoteSuccessCallback(data){
        noteList.renderNoteItem(data);
    }

    postNoteFailHandler() {
        console.log('노트 작성에 실패했습니다.');
    }
}