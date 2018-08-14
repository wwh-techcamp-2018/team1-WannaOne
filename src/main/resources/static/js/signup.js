class SignUp {
    constructor() {
        this.emailIdInput = $('#email_id')
        this.emailDomainInput = $('#email_domain')
        this.passwordInput = $('#password');
        this.passwordCheckInput = $('#password_check');
        this.passwordCheckCaution = $('#password_check_caution');
        this.nameInput = $('#name');
        this.signUpButton = $('#signUpButton');

        this.passwordCheckInput.addEventListener('keyup', this.checkPassword.bind(this));
        this.signUpButton.addEventListener('click', this.handlerSignUpEvent.bind(this));
    }

    checkPassword() {
        const password = this.passwordInput.value;
        const passwordCheck = this.passwordCheckInput.value;

        if (password !== passwordCheck) {
            this.passwordCheckCaution.innerHTML = '비밀번호와 확인 비밀번호가 다릅니다.';
            this.passwordCheckCaution.style.color = 'red';
        } else {
            this.passwordCheckCaution.innerHTML = '';
        }
    }

    handlerSignUpEvent(evt) {
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
            onFailure: this.signUpFailureCallback
        });
    }

    signUpSuccessCallback() {
        document.location.href = '/';
    }

    signUpFailureCallback() {

    }
}

document.addEventListener("DOMContentLoaded", () => {
    const signUp = new SignUp();
});