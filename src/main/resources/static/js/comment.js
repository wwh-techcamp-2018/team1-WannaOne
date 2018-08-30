class Comment {
    constructor() {
        this.commentListUl = $('.comment-list');
        this.commentSection = $('.comment-section');
        this.commentListSection = $('.comment-list-section');
        this.addCommentBtn = $('#add-comment-button');
        this.commentInput = $('#comment-input');
        this.commentCount = $('#comment-count');
        this.openCommentBtn = $('.comment-fold');

        this.initEvent();
    }

    initEvent() {
        this.commentInput.addEventListener('keyup', ({keyCode}) => {
            if(keyCode === 13) {
                this.addCommentClickEventHandler();
            }
        });
        this.addCommentBtn.addEventListener('click', this.addCommentClickEventHandler.bind(this));
        this.commentListUl.addEventListener('click', this.deleteCommentClickEventHandler.bind(this));
        this.openCommentBtn.addEventListener('click', this.openCommentHandler.bind(this));
    }

    updateNoteInfo(note) {
        this.noteId = note.id;
    }

    fetchWriteComment(content, noteId, successCallback, failCallback) {
        fetchManager({
            url: `/api/notes/${noteId}/comments`,
            method: 'POST',
            headers: {'content-type': 'application/json'},
            body: JSON.stringify({
                content: content
            }),
            onSuccess: successCallback,
            onFailure: failCallback
        });
    }

    fetchDeleteComment(noteId, commentId, successCallback, failCallback) {
        fetchManager({
            url: `/api/notes/${noteId}/comments/${commentId}`,
            method: 'DELETE',
            headers: {'content-type': 'application/json'},
            onSuccess: successCallback,
            onFailure: failCallback
        });
    }

    openCommentHandler() {
        // 원래는 팝업창 띄우기로 했었는데 일단은 스크롤 내리는 걸로만 처리.
        $('.main-content').scroll(0, $('.main-content').offsetHeight);
    }

    addCommentClickEventHandler() {
        const content = this.commentInput.value;
        if(!content || !content.trim()) {
            this.commentInput.value = '';
            return;
        }
        const successCallback = (response) => {
            this.commentListUl.insertAdjacentHTML('beforeend', getCommentTemplate(response));
            this.commentListUl.lastElementChild.classList.add('is-writer');
            this.updateCommentCount();
            this.commentInput.value = '';
            console.log('댓글 작성에 성공했습니다.');
            $('.main-content').scroll(0, $('.main-content').scrollHeight);
        };
        const failCallback = () => {
            this.commentInput.value = '';
            console.log('댓글 작성에 실패했습니다.');
        };
        this.fetchWriteComment(content, this.noteId, successCallback.bind(this), failCallback)
    }

    deleteCommentClickEventHandler(e) {
        const target = e.target;
        if(target.tagName !== 'I' || !confirm('해당 댓글을 삭제하시겠습니까?')) {
            return;
        }
        const successCallback = () => {
            const commentItemLi = e.target.closest('li');
            commentItemLi.parentNode.removeChild(commentItemLi);
            this.updateCommentCount();
            console.log('댓글 삭제에 성공했습니다.');
        };
        const failCallback = () => {
            console.log('댓글 삭제에 실패했습니다.');
        };
        this.deleteComment(target, successCallback, failCallback);
    }

    deleteComment(deleteTarget, successCallback, failCallback) {
        const commentId = deleteTarget.closest('LI').dataset.commentId;
        this.fetchDeleteComment(this.noteId, commentId, successCallback, failCallback);
    }

    updateCommentCount() {
        this.commentCount.innerHTML = this.commentListUl.childElementCount;
    }

    getCommentListUl() {
        return this.commentListUl;
    }

    getCommentSection() {
        return this.commentSection;
    }

    getCommentListSection() {
        return this.commentListSection;
    }
}