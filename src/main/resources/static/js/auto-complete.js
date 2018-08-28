class AutoComplete {
    constructor() {
        this.searchUserInputEl = $('#search-user');
        this.autoCompleteListEl = $('.user-suggestion-ul');
        this.sharedInvitationListEl = $('#share-invitation-list');
        this.invitation = new Invitation();
        this.initEventListener();
    }

    initEventListener() {
        this.searchUserInputEl.addEventListener('keyup', this.autoCompleteKeyUpEventHandler.bind(this));
        this.searchUserInputEl.addEventListener('keydown', (evt) => {
            if(evt.keyCode === 13) {
                evt.preventDefault();
                const activeElement = this.getActiveElement();
                this.searchUserInputEl.value = activeElement.innerHTML;
                const name = activeElement.dataset.userName;
                if(this.addCheck(name)) {
                    alert('이미 추가된 유저입니다.');
                    this.clearAutoCompleteEl();
                    this.clearInput();
                    return;
                }
                this.invitation.precheckInvitationAddress();
                this.clearAutoCompleteEl();
                this.clearInput();
            }
        });
    }

    addCheck(name) {
        let isAdded = false;
        const invitationUsers = this.sharedInvitationListEl.children;
        for(let i=0; i<invitationUsers.length; i++) {
            if(invitationUsers[i].firstChild.textContent == name) {
                isAdded = true;
            }
        }
        return isAdded;
    }

    focusAutoCompleteText(index) {
        this.autoCompleteListEl.children[index].classList.toggle('focus');
    }

    getActiveElement() {
        return $('.user-suggestion-ul > .focus');
    }

    clearAutoCompleteEl() {
        this.autoCompleteListEl.innerHTML = '';
    }

    clearInput() {
        this.searchUserInputEl.value = '';
    }

    autoCompleteKeyUpEventHandler({keyCode}) {
        if (keyCode === 38 || keyCode === 40 || keyCode === 13) {
            this.handleKeyUp(keyCode);
        } else {
            this.getAutoCompleteTextList();
        }
    }

    keyUpMoveAutoComplete(autoCompleteEl, activeElement) {
        if(!activeElement) {
            activeElement = autoCompleteEl.lastElementChild;
            activeElement.classList.toggle('focus');
            return;
        }
        activeElement.classList.toggle('focus');
        const previousElement = activeElement.previousElementSibling;
        if(previousElement) {
            previousElement.classList.toggle('focus');
            return;
        }
        autoCompleteEl.lastElementChild.classList.toggle('focus');
    }

    keyDownMoveAutoComplete(autoCompleteEl, activeElement) {
        if(!activeElement) {
            activeElement = autoCompleteEl.firstElementChild;
            activeElement.classList.toggle('focus');
            return;
        }
        activeElement.classList.toggle('focus');
        const nextElement = activeElement.nextElementSibling;
        if(nextElement) {
            nextElement.classList.toggle('focus');
            return;
        }
        autoCompleteEl.firstElementChild.classList.toggle('focus');
    }

    handleKeyUp(keyCode) {
        const autoCompleteEl = this.autoCompleteListEl;
        let activeElement = this.getActiveElement();
        if(keyCode === 38) { // up
            this.keyUpMoveAutoComplete(autoCompleteEl, activeElement);
        }
        else if(keyCode === 40) { // down
            this.keyDownMoveAutoComplete(autoCompleteEl, activeElement);
        }
    }

    getAutoCompleteTextList() {
        const searchText = this.searchUserInputEl.value;
        if(!searchText) {
            this.clearAutoCompleteEl();
            return;
        }
        fetchManager({
            url: `/api/users/search/${searchText}`,
            method: 'GET',
            headers: {'content-type': 'application/json'},
            onSuccess: (autoCompleteUsers) => {
                this.clearAutoCompleteEl();
                this.appendAutoCompleteTexts(autoCompleteUsers);
                this.focusAutoCompleteText(0);
            },
            onFailure: (response) => { console.log('유저 목록을 받아오는데 실패했습니다.');}
        });
    }

    appendAutoCompleteTexts(autoCompleteUsers) {
        autoCompleteUsers.forEach((user) => {
            this.autoCompleteListEl.insertAdjacentHTML('beforeend', getAutoCompleteListItem(user));
        })
    }
}