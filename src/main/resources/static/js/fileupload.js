function imageUpload(blob, callback) {

    let form = new FormData();
    form.append("file", blob);
    fetch("/aws/s3upload", {
        method: "POST",
        body: form,
        credentials: "same-origin"
    }).then((response) => {
        console.log("response received!");
        response.text().then((result) => {
            console.log(result);
            callback(result);
        })
    });
}