class NoteList {
    constructor() {
        this.noteListEl = $('.note-list');
        this.currentNoteIndex = 0;
    }

    renderNoteItem(note) {
        this.noteListEl.insertAdjacentHTML('beforeend', getNoteItemTemplate(note));
    }

    renderNoteList(notes) {
        this.clearNoteListSection();
        this.notes = notes;
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
        } else {
            //TODO: 빈 화면 처리 나중에 해주어야 함.
        }
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

    updateCurrentNoteData(updatedNote) {
        this.notes[this.currentNoteIndex] = updatedNote;
    }

    updateCurrentNoteItem() {
        this.removeCurrentNoteItem();
        this.appendNewNoteItem(this.notes[this.currentNoteIndex])
    }

    removeCurrentNoteItem() {
        this.noteListEl.removeChild(this.noteListEl.children[this.currentNoteIndex]);
    }

    appendNewNoteItem(note) {
        this.noteListEl.insertAdjacentHTML('afterbegin', getNoteItemTemplate(note));
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