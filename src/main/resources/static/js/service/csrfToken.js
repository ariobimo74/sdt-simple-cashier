function getCsrfTokenCookie() {
    const value = `; ${document.cookie}`
    const parts = value.split(`; ${'XSRF-TOKEN'}=`)
    return parts.length === 2 ? parts.pop().split(';').shift() : null
}