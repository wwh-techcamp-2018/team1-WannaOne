class MainApp {
    constructor() {
        this.noteBookListEl = $('.notebook-list');
        this.noteListEl = $('.note-list');
        this.noteSaveButton = $('#note-save-button');
        // this.noteDeleteButton = $('#note-delete-button');
        this.noteBook = new NoteBook(this.noteBookListEl);
        this.noteList = new NoteList();
        this.note = new NewNote();
        this.fetchNoteBookList();
        this.initEventListener();
    }

    /**
     * 노트북 클릭, 노트 클릭 시 발생할 이벤트 정의
     */
    initEventListener() {
        this.noteBookListEl.addEventListener('click', this.selectNoteBookEventHandler.bind(this));
        this.noteListEl.addEventListener("click", this.selectNoteEventHandler.bind(this));
        this.noteSaveButton.addEventListener('click', this.updateNoteEventHandler.bind(this));
    }

    /**
     * 제일 처음 노트북 로드하는 메소드
     */
    fetchNoteBookList() {
        const onSuccessCallBack = (notebooks) => {
            this.noteBook.initNoteBooks(notebooks);
            this.noteBook.setTitle();
            this.noteList.renderNoteList(this.noteBook.getNotes());
            this.noteList.focusNoteItem(0);
            this.note.renderNoteContent(this.noteList.getNote());
        };
        const onFailCallBack = () => {
            console.log("노트북 리스트를 받아오는데 실패했습니다.");
        };

        this.noteBook.initNoteBook(onSuccessCallBack.bind(this), onFailCallBack);
    }

    updateNoteEventHandler() {
        const onSuccessCallback = () => {
            this.fetchNoteList(this.noteBook.getNoteBookId());
        };
        const onFailureCallback = () => { console.log("die") };
        this.note.updateNote(onSuccessCallback, onFailureCallback);
    }


    fetchNoteList(noteBookId) {
        const onSuccessCallback = (notebook) => {
            this.noteList.renderNoteList(notebook.notes);
            this.noteList.focusNoteItem(0);
            this.note.renderNoteContent(this.noteList.getNote());
        };
        const onFailureCallback = () => {
            console.log("노트북 정보를 가져오는데 실패했습니다.");
        };
        this.noteList.fetchNoteList(noteBookId, onSuccessCallback.bind(this), onFailureCallback);
    }

    /**
     * 노트북을 선택한 경우의 이벤트 handler 노트북 포커스를 변경하고, 현재 노트북 index 변경 및 제목 변경
     * 현재 선택된 노트북의 업데이트된 정보를 받아오기 위해 ajax 요청.
     * @param e
     */
    selectNoteBookEventHandler(e){
        const targetNotebook = e.target.closest('li');
        this.noteBook.focusNoteBook(targetNotebook);
        this.noteBook.setTitle();
        this.fetchNoteList(this.noteBook.getNoteBookId());
    }

    /**
     * 노트가 선택된 경우의 이벤트 handler 선택된 노트가 현재의 노트가 아닌지 체크하고
     * 현재 노트의 인덱스를 구하고 포커스를 변경시킨 후 note 내용을 업데이트해준다.
     * @param e
     */
    selectNoteEventHandler(e) {
        const noteEl = e.target.closest('li');
        if (this.noteList.isNewItemClicked(noteEl)) {
            const index = getIndex(noteEl.parentElement.children, noteEl);
            this.noteList.focusNoteItem(index);
            this.note.renderNoteContent(this.noteList.getNote());
        }
    }
}

document.addEventListener("DOMContentLoaded", () => {
    new MainApp();
});