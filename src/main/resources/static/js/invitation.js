class Invitation {
    constructor() {
        this.invitationAddressInputEl = $('.share-invitation > input');
        this.notebookTitleEl = $('.side-bar-middle-notebook-title');
        this.invitationListEl = $('#share-invitation-list');
        this.invitationInputEl = $('.share-invitation > input');
        this.shareNotebookPopup = $('.share-notebook-popup');
        // this.invitationCancelBtn = $('.invitation-cancel-button');
        this.invitationSendBtn = $('.share-notebook-popup > button');
        this.shareNotebookPopup.style.display = "none";
        this.invitationValidationEl = $('#invite-validation');

        this.initEventListener();
    }

    initEventListener() {
        // this.invitationAddressInputEl.addEventListener('keyup', this.precheckInvitationAddressHandler.bind(this));
        this.invitationListEl.addEventListener('click', this.invitationCancelHandler.bind(this));
        this.invitationSendBtn.addEventListener('click', this.sendInvitationHandler.bind(this));
    }

    hideInvitationValidationMessage() {
        this.invitationValidationEl.style.display = 'none';
    }

    showInvitationValidationMessage(text) {
        this.invitationValidationEl.innerHTML = text;
        this.invitationValidationEl.style.display = 'block';
        setTimeout(this.hideInvitationValidationMessage.bind(this), 2000);
    }

    precheckInvitationAddress() {
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

    invitationCancelHandler(e) {
        if (e.target.classList.contains('invitation-cancel-button')) {
            this.invitationListEl.removeChild(e.target.closest('li'));
        }
    }

    closeShareNotebookPopup() {
        this.shareNotebookPopup.style.display = 'none';
        this.invitationInputEl.innerText = "";
        this.invitationListEl.innerHTML = "";
    }

    sendInvitationHandler() {
        if (this.invitationListEl.childElementCount == 0) {
            return;
        }
        const successCallback = () => {
            alert('초대 요청이 전송되었습니다.');
            this.closeShareNotebookPopup();
        };
        const failCallback = (response) => {
            response.json().then((result) => this.showInvitationValidationMessage(result.message));
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
            onFailure: failCallback.bind(this)
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