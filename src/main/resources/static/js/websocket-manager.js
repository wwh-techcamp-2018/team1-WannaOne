let socket = new SockJS('/websocket');
let stompClient = Stomp.over(socket);


class WebSocketManager {
    constructor() {
        this.initWebSocket();
        this.notificationUl = $('.notification-ul');
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
        this.notificationUl.insertAdjacentHTML('beforeend', getNotificationItem(message));
    }
}