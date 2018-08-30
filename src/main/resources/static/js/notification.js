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

    updateNotificationCount() {
        this.notificationBtn.innerHTML = getNotificationNumber(this.notificationUl.childElementCount);
    }

    getMessages() {
        const successCallback = (messages) => {
            messages.forEach((message) => {
                this.notificationUl.insertAdjacentHTML('beforeend', getInvitationNotificationItem(message));
            });
            this.updateNotificationCount();
        };
        const failCallback = () => {
            console.log("알림 목록 가져오기 실패");
        };
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
            alert("공유노트북이 추가되었습니다.");
            console.log("노트북 공유 초대 수락 성공");
            window.location.reload(true);
        };
        const failCallback = () => {
            console.log("노트북 공유 초대 수락에 실패했습니다.");
        };
        this.fetchRespondInvitation(invitationId, 'ACCEPTED', successCallback, failCallback);
    }

    rejectInvitationHandler(e) {
        e.preventDefault();
        if(!confirm('공유 초대를 거절하시겠습니까?')) return;
        const liElement = e.target.closest('li');
        const invitationId = liElement.dataset.invitationId;
        const successCallback = () => {
            this.rejectNotification(liElement);
            this.updateNotificationCount();
            console.log("노트북 공유 초대 거절에 성공했습니다.");
        };
        const failCallback = () => {
            console.log("노트북 공유 초대 거절에 실패했습니다.");
        };
        this.fetchRespondInvitation(invitationId, 'REJECTED', successCallback, failCallback);
    }

    fetchRespondInvitation(invitationId, status, successCallback, failCallback) {
        fetchManager({
                url: '/api/invitations',
                method: 'POST',
                headers: {'content-type': 'application/json'},
                body: JSON.stringify({
                            response: status,
                            invitationId: invitationId
                        }),
                onSuccess: successCallback,
                onFailure: failCallback
        });
    }

    rejectNotification(liElement) {
        liElement.parentElement.removeChild(liElement);
    }
}