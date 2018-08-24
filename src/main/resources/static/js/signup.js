class SignUp {
    constructor() {
        this.emailIdInput = $('#email_id')
        this.emailDomainInput = $('#email_domain')
        this.emailCaution = $('#email_caution');
        this.passwordInput = $('#password');
        this.passwordCheckInput = $('#password_check');
        this.passwordCheckCaution = $('#password_check_caution');
        this.nameInput = $('#name');
        this.signUpButton = $('#signUpButton');
        this.cautionEl = $All('.caution');

        this.passwordCheckInput.addEventListener('keyup', this.displayPasswordCheckMessage.bind(this));
        this.signUpButton.addEventListener('click', this.handlerSignUpEvent.bind(this));
        $('.submit-btn').addEventListener('click', function(event){
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
            this.passwordCheckCaution.style.color = 'red';
            this.signUpButton.disabled = true;
        } else {
            this.passwordCheckCaution.innerHTML = '비밀번호와 확인 비밀번호가 일치합니다.';
            this.passwordCheckCaution.style.color = 'blue';
            this.signUpButton.disabled = false;
        }
    }

    handlerSignUpEvent(evt) {
        if(!this.checkPassword()) return;

        const emailId = this.emailIdInput.value;
        const emailDomain = this.emailDomainInput.value;
        const password = this.passwordInput.value;
        const name = this.nameInput.value;

        fetchManager({
            url: '/api/users',
            method: 'POST',
            headers: {'content-type': 'application/json'},
            body: JSON.stringify({
                email: emailId + '@' + emailDomain,
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
        this.cautionEl.forEach((caution) => {
            caution.style.display = 'none';
        });
        const status = response.status;
        response.json().then((response) => {
            if(status === 403) {
                //이메일 중복
                this.emailCaution.style.display = 'block';
                this.emailCaution.innerHTML = response.message;
            } else if (status == 400) {
                // validation error.
                response.errors.forEach((error) => {
                    const validationCaution = $(`#${error.fieldName}_caution`);
                    validationCaution.style.display = 'block';
                    validationCaution.innerHTML = error.errorMessage;
                });
            }
        });
    }
}

document.addEventListener("DOMContentLoaded", () => {
    const signUp = new SignUp();
});