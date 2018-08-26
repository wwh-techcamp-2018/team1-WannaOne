class NoteList {
    constructor() {
        this.noteListEl = $('.note-list');
        this.noteListNum = $('.side-bar-middle-notebook-meta');
        this.currentNoteIndex = 0;
        this.initEvent();
    }

    initEvent() {
        this.noteListEl.addEventListener('dragstart', this.updateNoteOnDragStartEventHandler.bind(this));
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
        this.noteListNum.innerHTML = `${notes.length}개의 노트`;
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
}