class Comment {
    constructor() {
        this.commentListUl = $('.comment-list');
        this.commentSection = $('.comment-section');
        this.commentListSection = $('.comment-list-section');
        this.addCommentBtn = $('#add-comment-button');
        this.commentInput = $('#comment-input');
        this.commentCount = $('#comment-count');

        this.initEvent();
    }

    initEvent() {
        this.commentListUl.addEventListener('click', this.deleteCommentClickEventHandler.bind(this));
        this.addCommentBtn.addEventListener('click', this.addCommentClickEventHandler.bind(this));
        this.commentInput.addEventListener('keyup', ({keyCode}) => {
            if(keyCode === 13) {
                this.addCommentClickEventHandler();
            }
        });
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

    addCommentClickEventHandler() {
        const content = this.commentInput.value;
        const successCallback = (response) => {
            this.commentListUl.insertAdjacentHTML('beforeend', getCommentTemplate(response));
            this.updateCommentCount();
            this.commentInput.value = '';
            console.log('댓글 작성에 성공했습니다.');
        };
        const failCallback = () => {
            this.commentInput.value = '';
            console.log('댓글 작성에 실패했습니다.');
        }
        this.fetchWriteComment(content, this.noteId, successCallback.bind(this), failCallback)
    }

    deleteCommentClickEventHandler(e) {
        const target = e.target;
        if(target.tagName !== 'I' || !confirm('해당 댓글을 삭제하시겠습니까?')) {
            return;
        }
        const successCallback = (response) => {
            const commentItemLi = e.target.closest('li');
            commentItemLi.parentNode.removeChild(commentItemLi);
            this.updateCommentCount();
            console.log('댓글 삭제에 성공했습니다.');
        };
        const failCallback = () => {
            console.log('댓글 삭제에 실패했습니다.');
        };
        this.deleteComment(target, successCallback.bind(this), failCallback);
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