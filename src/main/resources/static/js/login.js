class Login {
    constructor() {
        this.loginButton = $('#loginButton');
        this.emailInput = $('#email');
        this.passwordInput = $('#password');

        this.loginButton.addEventListener('click', this.handlerLoginEvent().bind(this))
    }

    handlerLoginEvent(evt) {
        const email = this.emailInput.value;
        const password = this.passwordInput.value;

        evt.preventDefault();
        fetchManager({
            url: 'api/users/login',
            method: 'POST',
            headers: {'content-type': 'application/json'},
            body: JSON.stringify({
                email: email,
                password: password
            }),
            onSuccess: this.loginSuccessCallback,
            onFailure: this.loginFailureCallback
        });
    }

    loginSuccessCallback() {
        document.location.href = '/';
    }

    loginFailureCallback() {

    }
}