class MainApp {
    constructor() {
        this.notebookTitleInput = $('#notebook-title-input');
        this.noteBookListEl = $('.notebook-list');
        this.noteListEl = $('.note-list');
        this.addNoteBtn = $('#add-note-btn');
        this.noteSaveBtn = $('#note-save-button');
        this.noteDeleteBtn = $('#note-delete-button');
        this.logoutBtn = $('#logout');


        this.noteBook = new NotebookList(this.noteBookListEl);
        this.noteList = new NoteList();
        this.note = new Note();
        this.notification = new Notification();
        this.autoComplete = new AutoComplete();
        this.invitation = new Invitation();

        this.initMainPage();
        this.initEventListener();
        this.initAutoCompleteEventListener();
        this.webSocketManager = new WebSocketManager();
    }

    /**
     * 노트북 클릭, 노트 클릭 시 발생할 이벤트 정의
     */
    initEventListener() {
        this.notebookTitleInput.addEventListener('keyup', this.createNewNotebookEventHandler.bind(this));
        this.noteBookListEl.addEventListener('click', this.selectNoteBookEventHandler.bind(this));
        this.noteListEl.addEventListener("click", this.selectNoteEventHandler.bind(this));
        this.noteBook.hideNoteListButton.addEventListener("click", this.toggleNoteListHandler.bind(this));
        this.addNoteBtn.addEventListener("click", this.createNewNoteEventHandler.bind(this));
        this.noteSaveBtn.addEventListener('click', this.updateNoteEventHandler.bind(this));
        this.noteDeleteBtn.addEventListener('click', this.deleteNoteEventHandler.bind(this));
        this.logoutBtn.addEventListener('click', this.logoutEventHandler.bind(this));
        this.noteBookListEl.addEventListener('drop', this.updateNoteOnDragOverInNoteBookEventHandler.bind(this))
        this.noteBookListEl.addEventListener('dragover', (evt) => { evt.preventDefault(); })
    }

    initAutoCompleteEventListener() {
        this.autoComplete.getSearchUserInputEl().addEventListener('keyup', this.autoComplete.autoCompleteKeyUpEventHandler.bind(this.autoComplete));
        this.autoComplete.getSearchUserInputEl().addEventListener('keydown', (evt) => {
            if(evt.keyCode === 13) {
                evt.preventDefault();
                const activeElement = this.autoComplete.getActiveElement();
                if(!activeElement) {
                    this.invitation.showInvitationValidationMessage('유저 정보를 찾을 수 없습니다.');
                    return;
                }
                this.autoComplete.getSearchUserInputEl().value = activeElement.innerHTML;
                const name = activeElement.dataset.userName;
                if(this.autoComplete.addCheck(name)) {
                    alert('이미 추가된 유저입니다.');
                    this.autoComplete.clearAutoCompleteEl();
                    this.autoComplete.clearInput();
                    return;
                }
                this.invitation.precheckInvitationAddress();
                this.autoComplete.clearAutoCompleteEl();
                this.autoComplete.clearInput();
            }
        });
    }

    updateNoteOnDragOverInNoteBookEventHandler(evt){
        const targetNotebook = evt.target.closest('li');
        if(!targetNotebook) {
            return;
        }
        const noteId = evt.dataTransfer.getData("noteId");
        const noteBookId = targetNotebook.dataset.notebookId;
        const successCallback = () => {
            this.noteBook.focusNoteBook(targetNotebook);
            this.noteBook.setTitle();
            this.renewNoteList(this.noteBook.getNoteBookId());
        };
        const failCallback = () => {
            console.log('노트를 이동시키는데 실패했습니다.');
        };
        this.noteList.fetchNoteUpdateParentNoteBook(noteId, noteBookId, successCallback, failCallback);
    }

    /**
     * 제일 처음 노트북 로드하고 메인 페이지를 렌더링하는 메소드
     */
    initMainPage() {
        const successCallback = (user) => {
            this.noteBook.setOwner(user);
            this.renewNotebookList();
            $('.profile-thumbnail').src = user.photoUrl;
            $('.profile-name').innerHTML = user.name;
            $('.comment-write-thumbnail').src = user.photoUrl;
        }
        fetchManager({
            url: '/api/users/profile',
            method: 'GET',
            onSuccess: successCallback,
            onFailure: () => {
                console.log('유저 정보를 가져오는데 실패했습니다.');
            }
        });

    }

    createNewNotebookEventHandler(e) {
        if(e.keyCode !== 13) {
            return;
        }
        const successCallback = (newNotebook) => {
            this.noteBook.addNoteBookSuccessCallback(newNotebook);
            this.noteBook.setTitle();
            this.noteList.renderNoteList(this.noteBook.getNotes());
            this.noteList.focusNoteItem(0);
            this.note.renderNoteContent(this.noteList.getNote());
        };
        const failCallback = (error) => {
            this.noteBook.addNoteBookFailureCallback(error);
        };
        this.noteBook.createNewNotebook(successCallback, failCallback);
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
        if(!confirm('해당 노트를 삭제하시겠습니까?')) {
            return;
        }
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
        const target = e.target;
        //노트북 삭제 버튼이 클릭된 경우
        if(target.classList.contains('fa-trash-alt') && confirm('해당 노트북을 삭제하시겠습니까?')) {
            const successCallback = () => {
                this.renewNotebookList();
            };
            const failCallback = () => {
                console.log('노트북 삭제에 실패했습니다.');
            };
            this.noteBook.deleteNoteBook(target, successCallback, failCallback);
            return;
        }
        const targetNotebook = target.closest('li');
        if(!targetNotebook) {
            return;
        }
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

    toggleNoteListHandler() {
        this.noteBook.toggleHideNoteListButton();
        this.noteList.toggleNoteListBar();
        this.note.toggleExpandNoteContent();
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

    renewNotebookList() {
        const successCallback = (notebooks) => {
            if (!notebooks.length) {
                console.log('노트북이 존재하지 않습니다.');
                return;
            }
            this.noteBook.clearNoteBookList();
            this.noteBook.renderNotebooks(notebooks);
            this.noteBook.setTitle();
            this.noteList.renderNoteList(this.noteBook.getNotes());
            this.noteList.focusNoteItem(0);
            this.note.renderNoteContent(this.noteList.getNote());
        };
        const failCallback = () => {
            console.log("최초의 노트북 리스트를 받아오는데 실패했습니다.");
        };

        this.noteBook.fetchNotebookList(successCallback.bind(this), failCallback);
    }

    renewNoteList(noteBookId) {
        const successCallback = (notebook) => {
            this.noteList.renderNoteList(notebook.notes);
            const isFocus = this.noteList.focusNoteItem(0);
            if(!isFocus) {
                this.note.clearNoteSection();
            } else {
                this.note.renderNoteContent(this.noteList.getNote());
            }

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