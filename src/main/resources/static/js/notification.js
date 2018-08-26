class Notification {
    constructor() {
        this.notificationBtn = $('#notification-btn');
        this.removeNotificationBtn = $('#remove-notification-btn');
        this.notificationEl = $('.notification-wrapper');
        this.initEventListener();
    }

    initEventListener() {
        this.notificationBtn.addEventListener('click', this.toggleNotificationHandler.bind(this));
        this.removeNotificationBtn.addEventListener('click', this.toggleNotificationHandler.bind(this));
    }

    toggleNotificationHandler() {
        this.notificationEl.classList.toggle('notification-hide');
    }
}