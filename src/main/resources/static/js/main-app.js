class MainApp {
    constructor() {
        this.noteBookListEl = $('.notebook-list');
        this.noteListEl = $('.note-list');
        this.addNoteBtn = $('#add-note-btn');
        this.noteSaveBtn = $('#note-save-button');
        this.noteDeleteBtn = $('#note-delete-button');
        this.logoutBtn = $('#logout');

        this.noteBook = new NotebookList(this.noteBookListEl);
        this.noteList = new NoteList();
        this.note = new Note();
        this.initMainPage();
        this.initEventListener();
    }

    /**
     * 노트북 클릭, 노트 클릭 시 발생할 이벤트 정의
     */
    initEventListener() {
        this.noteBookListEl.addEventListener('click', this.selectNoteBookEventHandler.bind(this));
        this.noteListEl.addEventListener("click", this.selectNoteEventHandler.bind(this));
        this.addNoteBtn.addEventListener("click", this.createNewNoteEventHandler.bind(this));
        this.noteSaveBtn.addEventListener('click', this.updateNoteEventHandler.bind(this));
        this.noteDeleteBtn.addEventListener('click', this.deleteNoteEventHandler.bind(this));
        this.logoutBtn.addEventListener('click', this.logoutEventHandler.bind(this));
    }

    /**
     * 제일 처음 노트북 로드하고 메인 페이지를 렌더링하는 메소드
     */
    initMainPage() {
        const successCallback = (notebooks) => {
            if (!notebooks.length) {
                console.log('노트북이 존재하지 않습니다.');
                return;
            }
            this.noteBook.renderNotebooks(notebooks);
            this.noteBook.setTitle();
            this.noteList.renderNoteList(this.noteBook.getNotes());
            this.noteList.focusNoteItem(0);
            this.note.renderNoteContent(this.noteList.getNote());
        };
        const failCallback = () => {
            console.log("최초의 노트북 리스트를 받아오는데 실패했습니다.");
        };

        this.noteBook.initNotebookList(successCallback.bind(this), failCallback);
    }

    createNewNoteEventHandler() {
        const successCallback = () => {
            this.renewNoteList(this.noteBook.getNoteBookId());
        };
        const failCallback = () => {
            console.log('새로운 노트 생성에 실패했습니다..');
        };
        this.noteList.createNewNote(this.noteBook.getNoteBookId(), successCallback, failCallback);
    }

    updateNoteEventHandler() {
        const successCallback = () => {
            this.renewNoteList(this.noteBook.getNoteBookId());
        };
        const failCallback = () => {
            console.log('노트 업데이트에 실패했습니다.');
        };
        this.note.updateNote(successCallback, failCallback);
    }

    deleteNoteEventHandler() {
        const successCallback = () => {
            this.renewNoteList(this.noteBook.getNoteBookId());
        };
        const failCallback = () => {
            console.log('노트 업데이트에 실패했습니다.');
        };
        this.note.deleteNote(successCallback, failCallback);
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
        this.renewNoteList(this.noteBook.getNoteBookId());
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
    logoutEventHandler(e) {
        fetchManager({
                    url: '/api/users/logout',
                    method: 'POST',
                    onSuccess: this.logoutSuccess,
                    onFailure: this.logoutFailure
                });
    }
    logoutSuccess() {
        console.log("success");
        document.location.href="/login.html";
    }
    logoutFailure() {
        console.log("fail");
    }


    renewNoteList(noteBookId) {
        const successCallback = (notebook) => {
            this.noteList.renderNoteList(notebook.notes);
            this.noteList.focusNoteItem(0);
            this.note.renderNoteContent(this.noteList.getNote());
        };
        const failCallback = () => {
            console.log("노트북 정보를 가져오는데 실패했습니다.");
        };
        this.noteList.fetchNoteList(noteBookId, successCallback.bind(this), failCallback);
    }

}

document.addEventListener("DOMContentLoaded", () => {
    new MainApp();
});