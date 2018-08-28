class Invitation {
    constructor() {
        this.invitationAddressInputEl = $('.share-invitation > input');
        this.notebookTitleEl = $('.side-bar-middle-notebook-title');
        this.invitationListEl = $('#share-invitation-list');
        // this.invitationCancelBtn = $('.invitation-cancel-button');
        this.invitationSendBtn = $('.share-notebook-popup > button');

        this.initEventListener();
    }

    initEventListener() {
        this.invitationAddressInputEl.addEventListener('keyup', this.precheckInvitationAddressHandler.bind(this));
        // this.invitationListEl.addEventListener('click', this.invitationCancelHandler.bind(this));
        this.invitationSendBtn.addEventListener('click', this.sendInvitationHandler.bind(this));
    }

    precheckInvitationAddressHandler(e) {
        if(e.keyCode !== 13) {
            return;
        }
        const successCallback = (invitedGuest) => {
            console.log(invitedGuest);
            this.invitationListEl.insertAdjacentHTML('beforeend', getInvitedGuestItemTemplate(invitedGuest));
        };
        const failCallback = () => {
            console.log("cannot invite this user");
            //TODO 초대할 수 없는 사람일 경우 에러메시지 띄우기
        };
        this.precheckAddress(successCallback, failCallback);
    }

    // invitationCancelHandler(e) {
    //     this.invitationListEl.removeChild(e.target.closest('li'));
    // }

    sendInvitationHandler() {
        if (this.invitationListEl.childElementCount == 0) {
            return;
        }
        const successCallback = () => {
            console.log('invitation 성공');
            //TODO 팝업창 닫기
        };
        const failCallback = () => {
            console.log("invitation 실패");
        };
        this.sendInvitation(this.getInvitationData(), successCallback, failCallback);
    }

    precheckAddress(successCallback, failCallback) {
        const email = this.invitationAddressInputEl.value;
        const notebookId = this.notebookTitleEl.dataset.notebookId;
        const queryString = "?guestEmail=" + email + "&noteBookId=" + notebookId;

        fetchManager({
            url: "/api/users/invite" + queryString,
            method: "GET",
            onSuccess: successCallback,
            onFailure: failCallback
        });
    }

    sendInvitation(invitationData, successCallback, failCallback) {
        fetchManager({
            url: "/api/users/invite",
            method: "POST",
            headers: {'content-type': 'application/json'},
            body: JSON.stringify(invitationData),
            onSuccess: successCallback,
            onFailure: failCallback
        });
    }

    getInvitationData() {
        const guestIdList = this.collectInvitedGuests();
        const notebookId = this.notebookTitleEl.dataset.notebookId;
        return {
            notebookId: notebookId,
            guestIdList: guestIdList
        };
    }

    collectInvitedGuests() {
        let guestIdList = [];
        Array.from(this.invitationListEl.children).forEach((liElement) => guestIdList.push(liElement.dataset.guestid));
        return guestIdList;
    }
}