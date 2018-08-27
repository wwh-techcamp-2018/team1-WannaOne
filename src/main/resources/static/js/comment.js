class Comment {
    constructor() {
    }

    fetchWriteComment(content, noteId, successCallback, failCallback) {
        fetchManager({
            url: `/api/notes/${noteId}/comments`,
            method: 'POST',
            headers: {'content-type': 'application/json'},
            body: JSON.stringify({
                content: content
            }),
            onSuccess: successCallback,
            onFailure: failCallback
        });
    }
}