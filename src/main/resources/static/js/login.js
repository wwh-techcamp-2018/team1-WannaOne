document.addEventListener("DOMContentLoaded", () => {
    new Login();
})

class Login {
    constructor() {
        this.loginButton = $('#loginButton');
        this.emailInput = $('#email');
        this.passwordInput = $('#password');
        this.passwordCaution = $('#password-validation');
        this.cautionEl = $All('.caution');

        this.loginButton.addEventListener('click', this.handlerLoginEvent.bind(this));
    }

    handlerLoginEvent(evt) {
        const email = this.emailInput.value;
        const password = this.passwordInput.value;

        evt.preventDefault();
        fetchManager({
            url: '/api/users/login',
            method: 'POST',
            headers: {'content-type': 'application/json'},
            body: JSON.stringify({
                email: email,
                password: password
            }),
            onSuccess: this.loginSuccessCallback,
            onFailure: this.loginFailureCallback.bind(this)
        });
    }

    loginSuccessCallback() {
        document.location.href = '/main.html';
    }

    loginFailureCallback(response) {
        this.hideValidationError();
        const status = response.status;
        response.json().then((response) => {
            if(status === 401) {
                //아이디 또는 비밀번호가 잘못된 경우
                this.passwordCaution.style.display = 'block';
                this.passwordCaution.innerHTML = response.message;
            } else if(status === 400) {
                //비밀번호나 이메일 양식이 맞지 않는 경우
                response.errors.forEach((error) => {
                    const validationEl = $(`#${error.fieldName}-validation`);
                    validationEl.style.display = 'block';
                    validationEl.innerHTML = error.errorMessage;
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