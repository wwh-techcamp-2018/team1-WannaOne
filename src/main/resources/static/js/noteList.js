class NoteList {
    constructor() {
        this.noteListSection = $('.note-list');
        this.initNoteList();
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

    renderNoteList(data) {
        this.clearNoteListSection();
        data.forEach((note) => this.renderNoteItem(note));
        this.focusFirstNoteItem();
    }

    renderNoteItem(note) {
        this.noteListSection.insertAdjacentHTML('beforeend', this.noteItemFormatter(note));
    }

    noteItemFormatter(note) {
        return this.noteItemTemplate(note, dateFormatter(note.registerDatetime));
    }

    noteItemTemplate(note, registerDatetime) {
       return `<li data-note-id="${note.id}">
                <div class="note-item">`
              + this.noteItemContentTemplate(note, registerDatetime)
              + `</div></li>`;
    }

    noteItemContentTemplate(note, datetime) {
       return `<div class="note-list-title">${note.title}</div>
               <div class="note-list-snippet"><span>${datetime} </span>${note.text}</div>`;
    }

    updateNoteItem(note) {
        const noteItem = $('.note-item-focus')
        noteItem.innerHTML = '';
        noteItem.insertAdjacentHTML('beforeend', this.noteItemUpdateFormatter(note));
    }

    noteItemUpdateFormatter(note) {
        return this.noteItemContentTemplate(note, dateFormatter(note.updateDatetime));
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
}