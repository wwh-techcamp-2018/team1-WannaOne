class NoteList {
    constructor() {
        this.noteListBar = $('.side-bar-middle');
        this.noteListEl = $('.note-list');
        this.noteListCount = $('.note-list-count');
        this.currentNoteIndex = 0;
        this.shareNotebookPopup = $('.share-notebook-popup');
        this.shareNotebookPopup.style.display = "none";
        this.invitationInputEl = $('.share-invitation > input');
        this.invitationListEl = $('#share-invitation-list');
        $('.share-notebook-open-button').addEventListener("click", this.openShareNotebookPopupHandler.bind(this));
        document.addEventListener("click", this.closeShareNotebookPopupHandler.bind(this));

        this.initEvent();
    }

    initEvent() {
        this.noteListEl.addEventListener('dragstart', this.updateNoteOnDragStartEventHandler.bind(this));
    }

    toggleNoteListBar() {
        this.noteListBar.classList.toggle('note-list-hide');
    }

    updateNoteOnDragStartEventHandler(evt) {
        evt.dataTransfer.setData("noteId", evt.target.dataset.noteId);
    }

    fetchNoteUpdateParentNoteBook(noteId, noteBookId, successCallback, failCallback) {
        fetchManager({
            url: `/api/notes/${noteId}/notebooks/${noteBookId}`,
            method: 'PATCH',
            headers: {'content-type': 'application/json'},
            onSuccess: successCallback,
            onFailure: failCallback
        });
    }

    renderNoteItem(note) {
        this.noteListEl.insertAdjacentHTML('beforeend', getNoteItemTemplate(note));
    }

    renderNoteList(notes) {
        this.clearNoteListSection();
        this.notes = notes;
        this.noteListCount.innerHTML = `${notes.length}개의 노트`;
        this.notes.forEach((note) => this.renderNoteItem(note));
    }

    clearNoteListSection() {
        this.noteListEl.innerHTML = '';
    }

    fetchNoteList(noteBookId, successCallback, failureCallback) {
        fetchManager({
            url: `/api/notebooks/${noteBookId}`,
            method: 'GET',
            onSuccess: successCallback,
            onFailure: failureCallback
        });
    }

    focusNoteItem(index) {
        if (this.noteListEl.children.length > 0) {
            const existFocusNote = $('.note-item-focus');
            if(existFocusNote) {
                existFocusNote.classList.toggle('note-item-focus');
            }
            this.noteListEl.children[index].firstElementChild.classList.add('note-item-focus');
            this.currentNoteIndex = index;
            return true;
        }
        return false;
    }

    getNoteId() {
        return this.notes[this.currentNoteIndex].id;
    }

    getNote() {
        return this.notes[this.currentNoteIndex];
    }

    isNewItemClicked(liElement) {
        return $('.note-item-focus') !== liElement.firstElementChild;
    }


    // 새 노트 저장
    createNewNote(noteBookId, successCallBack, failCallBack) {
        if(!noteBookId) {
            console.log('노트북이 선택되지 않았습니다.');
            return;
        }

        fetchManager({
            url: `/api/notes/notebook/${noteBookId}`,
            method: 'POST',
            headers: {'content-type': 'application/json'},
            onSuccess: successCallBack,
            onFailure: failCallBack
        })
    }

    openShareNotebookPopupHandler() {
        this.shareNotebookPopup.style.display = 'block';
    }

    closeShareNotebookPopupHandler(e) {
        if ($('.share-notebook-open-button').contains(e.target) || this.shareNotebookPopup.contains(e.target)) {
            return;
        }
//        this.clearShareNotebookPopup(); <- 이거 나중에 붙여주세요
        this.shareNotebookPopup.style.display = 'none';
    }

    clearShareNotebookPopup() {
        this.invitationInputEl.innerText = "";
        this.invitationListEl.innerHTML = "";
    }
}