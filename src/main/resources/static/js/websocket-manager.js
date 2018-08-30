let socket = new SockJS('/websocket');
let stompClient = Stomp.over(socket);


class WebSocketManager {
    constructor() {
        this.initWebSocket();
        this.notificationUl = $('.notification-ul');
        this.notificationBtn = $('#notification-btn');
    }

    initWebSocketCallback(webSocketEvents) {
        this.acceptCallback = webSocketEvents.acceptCallback;
        this.updateSharedAddNoteCallback = webSocketEvents.updateSharedAddNoteCallback;
    }

    initWebSocket() {
        const successCallback = (user) => {
            this.user = user;
            this.connect()
        }
        fetchManager({
            url: '/api/users/profile',
            method: 'GET',
            onSuccess: successCallback,
            onFailure: () => {
                console.log('유저 정보를 가져오는데 실패했습니다.');
            }
        });
    }

    connect() {
        const user = this.user;
        const handlerNotificationMessage = this.handlerNotificationMessage.bind(this);
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe(`/topic/users/${user.id}`, function (notification) {
                handlerNotificationMessage(JSON.parse(notification.body));
            });
        });
    }

    disconnect() {
        if (stompClient !== null) {
            stompClient.disconnect();
        }
        console.log("Disconnected");
    }

    handlerNotificationMessage(message) {
        let notificationMessageEl;
        if(message.type === 'WRITE_NOTIFICATION') {
            notificationMessageEl = getWriteNotificationItem(message);
            this.updateSharedAddNoteCallback(message.id);
        } else if(message.type === 'INVITATION') {
            notificationMessageEl = getNotificationItem(message);
        } else if(message.type === 'ACCEPT') {
            notificationMessageEl = getNotificationItem(message);
            this.acceptCallback();
        }
        this.notificationUl.insertAdjacentHTML('beforeend', notificationMessageEl);
        this.notificationBtn.innerHTML = getNotificationNumber(this.notificationUl.children.length);
    }
}