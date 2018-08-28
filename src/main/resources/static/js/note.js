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
        this.commentSection = $('.comment-section');
        this.commentListSection = $('.comment-list-section');
        this.addCommentBtn = $('#add-comment-button');
        this.commentInput = $('#comment-input');

        this.initEvent();
    }

    initEvent() {
        this.addCommentBtn.addEventListener('click', this.addCommentClickEventHandler.bind(this));
        this.commentInput.addEventListener('keyup', ({keyCode}) => {
            if(keyCode === 13) {
                this.addCommentClickEventHandler();
            }
        });
        this.editSection.addEventListener('click', this.showEditor.bind(this));
        document.addEventListener('click', this.hideEditor.bind(this));
        //this.editSection.addEventListener('focusout', this.hideEditor.bind(this));
    }

    toggleExpandNoteContent() {
        this.noteContent.classList.toggle('main-content-expand');
    }

    addCommentClickEventHandler() {
        const content = this.commentInput.value;
        const successCallback = (response) => {
            this.commentListSection.firstElementChild.insertAdjacentHTML('beforeend', getCommentTemplate(response));
            this.commentListSection.firstChild.nodeValue = `댓글 총 ${this.commentListSection.firstElementChild.childElementCount}개`;
            console.log(`${response} 댓글 작성 성공!`);
            this.commentInput.value = '';
        };
        const failCallback = () => {
            console.log('댓글 작성에 실패했습니다.');
            this.commentInput.value = '';
        }
        this.comment.fetchWriteComment(content, this.note.id, successCallback, failCallback)
    }

    clearNoteSection() {
        this.noteSection.innerHTML = '';
        this.editSection.style.display = 'none';
        this.btns.style.display = 'none';
        this.commentSection.style.display = 'none';
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
        editor.moveCursorToStart();
        this.previewTabBtn.click();
        if (editor.getMarkdown() != "") {
            this.hideEditor();
        }
    }

    renderNote(note) {
        this.note = note;
        this.noteSection.insertAdjacentHTML('beforeend', getNoteSectionTemplate(note));
        editor.setValue(note.text);
        if (note.text == "") {
            this.showEditor();
        }
        this.commentListSection.innerHTML = getCommentListTemplate(note.comments);
        this.editSection.style.display = 'block';
        this.initSaveAndDeleteButton(note);
        this.commentSection.style.display = 'block';
    }

    hideEditor(e) {
        if (e!=null) {
            if (this.editSection.contains(e.target)) {
                return;
            }
            if (this.writeTabBtn.contains(e.target) || this.previewTabBtn.contains(e.target)) {
                return;
            }
        }
        if (this.previewTabBtn.classList.contains('te-tab-active') && $('.te-toolbar-section').style.display=='none') {
            return;
        }
        if (editor.getMarkdown() == "") {
            return;
        }
        editor.show();
        $('.te-toolbar-section').style.display = 'none';
        $('.tui-editor-defaultUI').style.border = 'none';
        $('.te-md-container .te-preview').style.padding = '0px';
        this.previewTabBtn.click();

    }

    showEditor(e) {
        if (e!= null) {
            if (this.writeTabBtn.contains(e.target) || this.previewTabBtn.contains(e.target)) {
                return;
            }
        }
        if (this.writeTabBtn.classList.contains('te-tab-active') && $('.te-toolbar-section').style.display=='block') {
            return;
        }
        editor.show();

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
}