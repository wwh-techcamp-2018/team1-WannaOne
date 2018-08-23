class NoteList {
    constructor() {
        this.noteListSection = $('.note-list');
        this.addNoteBtn = $('#add-note-btn');
        this.initNoteList();
        this.initButton();
    }

    initNoteList() {
        this.noteListSection.addEventListener("click", this.getNote.bind(this));
        this.renderNoteList(noteBookList.getNoteList(0)); //TODO: 노트북이 기본적으로 한개는 있으면 좋겠다.
    }

    getNote(e) {
        const liElement = e.target.closest('li');
        if (this.isNewItemClicked(liElement)) {
            const noteId = liElement.dataset.noteId;
            note.getNote(noteId);
            $('.note-item-focus').classList.toggle('note-item-focus');
            liElement.firstElementChild.classList.toggle('note-item-focus');
        }
    }

    renderNoteList(note) {
        this.clearNoteListSection();
        note.forEach((note) => this.renderNoteItem(note));
        this.focusFirstNoteItem();
    }

    renderNoteItem(note) {
        this.noteListSection.insertAdjacentHTML('afterbegin', this.noteItemFormatter(note));
    }

    noteItemFormatter(note) {
        return getNoteItemTemplate(note, dateFormatter(note.registerDatetime));
    }

    updateNoteItem(note) {
        const noteItem = $('.note-item-focus')
        noteItem.innerHTML = this.noteItemUpdateFormatter(note);
    }

    noteItemUpdateFormatter(note) {
        return getNoteItemContentTemplate(note, dateFormatter(note.updateDatetime));
    }

    isNewItemClicked(liElement) {
        return $('.note-item-focus') !== liElement.firstElementChild;
    }

    clearNoteListSection() {
        this.noteListSection.innerHTML = '';
    }

    focusFirstNoteItem() {
        if (this.noteListSection.children.length > 0) {
            this.noteListSection.firstElementChild.firstElementChild.classList.add('note-item-focus');
        } else {
            //TODO: 빈 화면 처리 나중에 해주어야 함.
        }
    }

    initButton() {
        this.addNoteBtn.addEventListener("click", () => this.saveHandler());
    }

    // 새 노트 저장
    saveHandler() {
        const noteBookId = $('.notebook-focus').dataset.notebookId;
        fetchManager({
                    url: `/api/notes/notebook/${noteBookId}`,
                    method: 'POST',
                    headers: {'content-type': 'application/json'},
                    onSuccess: this.addNoteSuccessCallback.bind(this),
                    onFailure: this.addNoteFailHandler
                })
    }

    addNoteSuccessCallback(note){
        this.renderNoteItem(note);
    }

    addNoteFailHandler() {
        console.log('노트 작성에 실패했습니다.');
    }
}