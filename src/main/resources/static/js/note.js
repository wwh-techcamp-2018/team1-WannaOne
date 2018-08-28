class Note {
    constructor() {
        this.noteContent = $('.main-content');
        this.noteSection = $('#note-section');
        this.editorEl = $('.te-ww-container .tui-editor-contents');
        this.editSection = $('#editSection');
        this.noteSection = $('#note-section');
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
        this.editSection.addEventListener('focusin', this.showEditor.bind(this));
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
        this.hideEditor();
    }

    renderNote(note) {
        this.note = note;
        this.noteSection.insertAdjacentHTML('beforeend', getNoteSectionTemplate(note));
        this.editorEl.innerHTML = note.text;
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
        }
        $('.te-toolbar-section').style.display = 'none';
        $('.tui-editor-defaultUI').style.border = 'none';
        $('.te-ww-container .tui-editor-contents').style.padding = '3px 16px 0px 0px';
    }

    showEditor() {
        $('.te-toolbar-section').style.display = 'block';
        $('.tui-editor-defaultUI').style.border = '1px solid #e5e5e5';
        $('.te-ww-container .tui-editor-contents').style.padding = '3px 25px 0px 25px';
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
        return this.editorEl.innerHTML;
    }

    getNoteId() {
        return this.note.id;
    }
}