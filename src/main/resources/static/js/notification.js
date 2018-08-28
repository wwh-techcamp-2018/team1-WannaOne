class Notification {
    constructor() {
        this.notificationBtn = $('#notification-btn');
        this.closeNotificationBtn = $('#close-notification-btn');
        this.notificationEl = $('.notification-wrapper');
        this.invitationAcceptBtn = $('.invitation-accept-button');
        this.invitationRejectBtn = $('.invitation-reject-button');
        this.initEventListener();
    }

    initEventListener() {
        this.notificationBtn.addEventListener('click', this.toggleNotificationHandler.bind(this));
        this.closeNotificationBtn.addEventListener('click', this.toggleNotificationHandler.bind(this));
        this.invitationAcceptBtn.addEventListener('click', this.acceptInvitationHandler.bind(this));
        this.invitationRejectBtn.addEventListener('click', this.rejectInvitationHandler.bind(this));
    }

    toggleNotificationHandler() {
        this.notificationEl.classList.toggle('notification-hide');
    }

    acceptInvitationHandler(e) {
        e.preventDefault();
        const liElement = e.target.closest('li');
        const noteBookId = liElement.dataset.notebookId;
        const successCallback = (notebook) => {
                    console.log("노트북 공유 초대 수락 성공. 제목: ", notebook.title);
                    //TODO: sharedNotebookList 다시 가져오거나 해당 노트북만 추가 해주기.
                    liElement.classList.add('checked');
                    liElement.firstElementChild.disabled = true;
                    liElement.firstElementChild.classList.add('checked');
                    liElement.firstElementChild.nextElementSibling.disabled = true;
                    liElement.firstElementChild.nextElementSibling.classList.add('checked');
                    alert("공유노트북이 추가되었습니다.");
                };
        const failCallback = () => {
                    console.log("노트북 공유 초대 수락에 실패했습니다.");
                };
        this.fetchAcceptInvitation(noteBookId, successCallback, failCallback);
    }

    fetchAcceptInvitation(noteBookId, successCallback, failCallback) {
        fetchManager({
                url: `/api/users/shared/${noteBookId}`,
                method: 'POST',
                onSuccess: successCallback,
                onFailure: failCallback
                });
    }

    rejectInvitationHandler(e) {
        e.preventDefault();
        const notification = e.target.closest('li').dataset.notebookId;
        //TODO: 작성해야 함
        //this.fetchRejectInvitation(noteBookId, successCallback, failCallback);
    }
}