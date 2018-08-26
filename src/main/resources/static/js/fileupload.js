function imageUpload(blob, callback) {

    let form = new FormData();
    form.append("file", blob);
    fetch("/aws/s3upload", {
        method: "POST",
        body: form,
        credentials: "same-origin"
    }).then((response) => {
        if(response.status === 500) {
            response.json().then((error) => {
                alert(error.message);
            })
            return;
        }
        response.text().then((result) => {
            callback(result);
        })
    });
}