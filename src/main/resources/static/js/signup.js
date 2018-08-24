class SignUp {
    constructor() {
        this.emailInput = $('#email')
        this.emailCaution = $('#email-validation');
        this.passwordInput = $('#password');
        this.passwordCheckInput = $('#passwordCheck');
        this.passwordCheckCaution = $('#passwordCheck-validation');
        this.nameInput = $('#name');
        this.signUpButton = $('#signUpButton');
        this.cautionEl = $All('.caution');

        this.passwordInput.addEventListener('keyup', this.displayPasswordCheckMessage.bind(this));
        this.passwordCheckInput.addEventListener('keyup', this.displayPasswordCheckMessage.bind(this));
        this.signUpButton.addEventListener('click', this.handlerSignUpEvent.bind(this));
        this.signUpButton.addEventListener('click', function(event){
            event.preventDefault();
        });
    }

    checkPassword() {
        const password = this.passwordInput.value;
        const passwordCheck = this.passwordCheckInput.value;

        if (password !== passwordCheck) {
            return false;
        }
        return true;
    }

    displayPasswordCheckMessage() {
        if(!this.checkPassword()) {
            this.passwordCheckCaution.innerHTML = '비밀번호와 확인 비밀번호가 다릅니다.';
        } else {
            this.passwordCheckCaution.innerHTML = '';
        }
        this.passwordCheckCaution.style.display = 'block';
    }

    handlerSignUpEvent(evt) {
        if(!this.checkPassword()) {
            this.hideValidationError();
            this.displayPasswordCheckMessage();
            return;
        }

        const email = this.emailInput.value;
        const password = this.passwordInput.value;
        const name = this.nameInput.value;

        fetchManager({
            url: '/api/users',
            method: 'POST',
            headers: {'content-type': 'application/json'},
            body: JSON.stringify({
                email: email,
                password: password,
                name: name
            }),
            onSuccess: this.signUpSuccessCallback,
            onFailure: this.signUpFailureCallback.bind(this)
        });
    }

    signUpSuccessCallback() {
        document.location.href = '/login.html';
    }

    signUpFailureCallback(response) {
        this.hideValidationError();
        const status = response.status;
        response.json().then((response) => {
            if(status === 403) {
                //이메일 중복
                this.emailCaution.style.display = 'block';
                this.emailCaution.innerHTML = response.message;
            } else if (status == 400) {
                // validation error.
                response.errors.forEach((error) => {
                    const validationCaution = $(`#${error.fieldName}-validation`);
                    validationCaution.style.display = 'block';
                    validationCaution.innerHTML = error.errorMessage;
                });
            }
        });
    }

    hideValidationError() {
        this.cautionEl.forEach((caution) => {
            caution.style.display = 'none';
        });
    }
}

document.addEventListener("DOMContentLoaded", () => {
    const signUp = new SignUp();
});