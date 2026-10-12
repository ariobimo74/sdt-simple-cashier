function downloadPdf(orderId) {
    var xhr = new XMLHttpRequest()

    xhr.open("GET", "/api/v1/order/download-pdf/" + orderId, true)
    xhr.responseType = "blob"

    xhr.onload = function () {
        if (xhr.status === 200) {
            var url = URL.createObjectURL(xhr.response)
            window.open(url, "_blank")

            savePdf("invoice.pdf", xhr)

            setTimeout(function () {
                URL.revokeObjectURL(url)
            }, 1000)
        } else {
            console.error("Download failed:", xhr.status)
        }
    }

    xhr.send()
}

function savePdf(filename, xhr) {
    disposition = xhr.getResponseHeader("Content-Disposition")
    if (disposition) {
        var filenameMatch = disposition.match(/filename="?([^"]+)"?/)

        if (filenameMatch && filenameMatch[1]) {
            filename = filenameMatch[1]
        }
    }

    var blob = xhr.response
    var url = window.URL.createObjectURL(blob)

    var link = document.createElement("a")
    link.href = url
    link.download = filename

    document.body.appendChild(link)
    link.click()

    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
}
