class Notification {
    constructor() {
        this.notificationBtn = $('#notification-btn');
        this.closeNotificationBtn = $('#close-notification-btn');
        this.notificationEl = $('.notification-wrapper');
        this.notificationUl = $('.notification-ul');
        this.invitationAcceptBtn = $('.invitation-accept-button');
        this.invitationRejectBtn = $('.invitation-reject-button');
        this.initEventListener();
        this.getMessages();
    }

    initEventListener() {
        this.notificationBtn.addEventListener('click', this.toggleNotificationHandler.bind(this));
        this.closeNotificationBtn.addEventListener('click', this.toggleNotificationHandler.bind(this));
        // this.invitationAcceptBtn.addEventListener('click', this.acceptInvitationHandler.bind(this));
        // this.invitationRejectBtn.addEventListener('click', this.rejectInvitationHandler.bind(this));
        this.notificationUl.addEventListener('click', this.responseInvitationHandler.bind(this))
    }

    responseInvitationHandler(evt) {
        const target= evt.target;
        if(target.className === 'invitation-accept-button' ) {
            this.acceptInvitationHandler(evt);
        } else if(target.className === 'invitation-reject-button') {
            this.rejectInvitationHandler(evt);
        }
    }

    toggleNotificationHandler() {
        this.notificationEl.classList.toggle('notification-hide');
    }

    getMessages() {
        const successCallback = (messages) => {
            messages.forEach((message) => {
                this.notificationUl.insertAdjacentHTML('beforeend', getNotificationItem(message));
            });
            this.notificationBtn.innerHTML = getNotificationNumber(messages.length);
        }
        const failCallback = () => {
            console.log("알림 목록 가져오기 실패");
        }
        this.fetchGetMessages(successCallback, failCallback);
    }

    fetchGetMessages(successCallback, failCallback) {
        fetchManager({
            url: `/api/users/invitations`,
            method: 'GET',
            onSuccess: successCallback,
            onFailure: failCallback
        });
    }

    acceptInvitationHandler(e) {
        e.preventDefault();
        const liElement = e.target.closest('li');
        const invitationId = liElement.dataset.invitationId;
        const successCallback = () => {
                    console.log("노트북 공유 초대 수락 성공");
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
        this.fetchAcceptInvitation(invitationId, successCallback, failCallback);
    }

    fetchAcceptInvitation(invitationId, successCallback, failCallback) {
        fetchManager({
                url: '/api/invitations',
                method: 'POST',
                headers: {'content-type': 'application/json'},
                body: JSON.stringify({
                                response: 'ACCEPTED',
                                invitationId: invitationId
                            }),
                onSuccess: successCallback,
                onFailure: failCallback
                });
    }

    rejectInvitationHandler(e) {
        e.preventDefault();
        const notification = e.target.closest('li').dataset.invitationId;
        //TODO: 작성해야 함
        //this.fetchRejectInvitation(noteBookId, successCallback, failCallback);
    }
}