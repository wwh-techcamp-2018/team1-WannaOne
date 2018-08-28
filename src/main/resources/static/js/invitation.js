class Invitation {
    constructor() {
        this.invitationAddressInputEl = $('.share-invitation > input');
        this.notebookTitleEl = $('.side-bar-middle-notebook-title');
        this.invitationListEl = $('#share-invitation-list');
        // this.invitationCancelBtn = $('.invitation-cancel-button');

        this.initEventListener();
    }

    initEventListener() {
        this.invitationAddressInputEl.addEventListener('keyup', this.precheckInvitationAddressHandler.bind(this));
        // this.invitationListEl.addEventListener('click', this.invitationCancelHandler.bind(this));
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
            console.log("fail했네요");
        };
        this.precheckAddress(successCallback, failCallback);
    }

    // invitationCancelHandler(e) {
    //     this.invitationListEl.removeChild(e.target.closest('li'));
    // }

    precheckAddress(successCallback, failCallback) {
        const email = this.invitationAddressInputEl.value;
        const notebookId = this.notebookTitleEl.dataset.notebookId;
        console.log(email);
        console.log(notebookId);

        const queryString = "?guestEmail=" + email + "&noteBookId=" + notebookId;

        fetchManager({
            url: "/api/users/invite" + queryString,
            method: "GET",
            onSuccess: successCallback,
            onFailure: failCallback
        });
    }

}