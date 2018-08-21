function $(selector) {
    return document.querySelector(selector);
}

function $All(selector) {
    return document.querySelectorAll(selector);
}

function fetchManager({ url, method, body, headers, onSuccess, onFailure}) {
    fetch(url, {
        method,
        body,
        headers,
        credentials: "same-origin"
    }).then((response) => {
        if(response.ok) {
            response.json().then((result) => onSuccess(result)).catch(() => onSuccess());
        } else {
            onFailure(response);
        }
    });
}

function datetimeFormatter(datetimeString) {
    const datetime = new Date(datetimeString);
    const data = {
        month: datetime.getMonth(),
        date: datetime.getDay(),
        hour: datetime.getHours(),
        minute: datetime.getMinutes()
    };
    return `${data.month}월 ${data.date}일 ${data.hour}시 ${data.minute}분`;
}

function dateFormatter(datetimeString) {
    const datetime = new Date(datetimeString);
    const data = {
        month: datetime.getMonth(),
        date: datetime.getDay(),
    };
    return `${data.month}월 ${data.date}일`;
}