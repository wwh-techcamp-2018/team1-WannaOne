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
            response.json().then((result) => onSuccess(result))
        } else {
            onFailure(response);
        }
    });
}