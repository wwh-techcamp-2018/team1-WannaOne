class Note {
    constructor() {
        this.noteContent = $('.main-content');
        this.noteSection = $('#note-section');
        this.editorEl = $('.te-md-container .te-editor .CodeMirror-code');
        this.editSection = $('#editSection');
        this.noteSection = $('#note-section');
        this.previewTabBtn = $('.te-markdown-tab-section .te-tab').firstElementChild.nextElementSibling;
        this.writeTabBtn = $('.te-markdown-tab-section .te-tab').firstElementChild;
        this.btns = $('.note-save-delete');

        this.comment = new Comment();
        this.initEvent();
    }

    initEvent() {
        document.addEventListener('click', this.modeSwitchHandler.bind(this));
    }

    toggleExpandNoteContent() {
        this.noteContent.classList.toggle('main-content-expand');
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

    clearNoteSection() {
        this.noteSection.innerHTML = '';
        this.editSection.style.display = 'none';
        this.btns.style.display = 'none';
        this.comment.getCommentListUl().innerHTML = '';
        this.comment.getCommentSection().style.display = 'none';
    }

    renderNoteContent(data) {
        if(!data) {
            //TODO 선택될 노트가 없는 경우. 예외처리!!
            return;
        }
        this.clearNoteSection();
        this.renderNote(data);
        this.comment.updateNoteInfo(data);
        editor.show();
        editor.moveCursorToStart();
        if (editor.getMarkdown() != "") {
            this.previewTabBtn.click();
            this.hideEditor();
        } else {
            this.writeTabBtn.click();
        }
    }

    renderNote(note) {
        this.note = note;
        this.noteSection.insertAdjacentHTML('beforeend', getNoteSectionTemplate(note));
        editor.setValue(note.text);
        if (note.text == "") {
            this.showEditor();
        }
        this.editSection.style.display = 'block';
        this.initSaveAndDeleteButton(note);
        this.renderComment();
    }

    renderComment() {
        this.comment.getCommentSection().style.display = 'block';
        this.note.comments.forEach((comment) => {
            this.comment.getCommentListUl().insertAdjacentHTML('beforeend', getCommentTemplate(comment));
        });
        this.comment.updateCommentCount();
    }

    hideEditor() {
        $('.te-toolbar-section').style.display = 'none';
        $('.tui-editor-defaultUI').style.border = 'none';
        $('.te-md-container .te-preview').style.padding = '0px';
        this.previewTabBtn.click();

    }

    showEditor() {
        $('.te-toolbar-section').style.display = 'block';
        $('.tui-editor-defaultUI').style.border = '1px solid #e5e5e5';
        $('.te-md-container .te-preview').style.padding = '0px 25px 0px 25px';
        this.writeTabBtn.click();
    }

    initSaveAndDeleteButton(note) {
        if(note.isWriter) {
            this.btns.style.display = 'inline-block';
            return;
        }
        this.btns.style.display = 'none';
    }

    getNoteTitle() {
        return this.noteSection.firstElementChild.value;
    }

    getNoteText() {
        return editor.getMarkdown();
    }

    getNoteId() {
        return this.note.id;
    }

    modeSwitchHandler(e) {
        const currentMode = $('.te-toolbar-section').style.display == 'none' ? 'view' : 'edit';

        if (currentMode == 'edit') {
            if (e!=null) {
                if (this.editSection.contains(e.target)) {
                    return;
                }
                if (this.writeTabBtn.contains(e.target) || this.previewTabBtn.contains(e.target)) {
                    return;
                }
            }
            if (this.previewTabBtn.classList.contains('te-tab-active')) {
                return;
            }
            if (editor.getMarkdown() == "") {
                return;
            }

            this.hideEditor();
            return;
        }

        if (currentMode == 'view') {
            if (!this.editSection.contains(e.target)) {
                return;
            }

            if (e!= null) {
                if (this.writeTabBtn.contains(e.target) || this.previewTabBtn.contains(e.target)) {
                    return;
                }
            }
            if (this.writeTabBtn.classList.contains('te-tab-active')) {
                return;
            }

            this.showEditor();
            return;
        }
    }
}