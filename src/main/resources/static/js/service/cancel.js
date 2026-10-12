$('document').ready(function() {
    $('#cancelBtn').click(function() {
        if (window.history.length > 1) window.history.back()
        else window.location.href = "/dashboard"
    })
})