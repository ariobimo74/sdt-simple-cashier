$(document).ready(function() {
    $('#side-menu').css({
        background: 'aliceblue',
        height: '100vh',
        'overflow-y': 'auto'
    })

    $('#page-wrapper').css({
        'margin-top': '5%'
    })
    
    generateMenu()

    let responseData = {
        acknowledge : "",
        responseCode : 0,
        responseMessage : ""
    }

    async function generateMenu() {
        await $.ajax({
            url: '/api/v1/menu',
            type: 'get',
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            success: function(result) {
                let menuResponse = {
                    data : null,
                    responseData : responseData
                }
                menuResponse = result

                if (menuResponse.responseData.responseCode == 200) {
                    $('#greetingName').text("Hello, " + result.data.greetingName + '!')
                    $('#navUser').text(result.data.greetingName)
                    
                    if (parseInt(result.data.menus.length) > parseInt(0)) {
                        for (i = 0; i < result.data.menus.length; i++) {
                            let menuTemp = result.data.menus[i]

                            $('#side-menu').append('<li class="class-' + menuTemp.menuClass + ' active"><a href=' + menuTemp.url + '><i class="' + menuTemp.iconClass + '"></i> ' + menuTemp.menuName + '</a>')
                            if (menuTemp.subMenu.length > 0) {
                                $(".class-" + menuTemp.menuClass).append('<ul class="' + menuTemp.menuClass + '-second ' + 'nav nav-second-level collapse in" aria-expanded="true" style="">')
                                for (let j = 0; j < menuTemp.subMenu.length; j++) {
                                    let menuTemp2 = menuTemp.subMenu[j]
                                    $('.' + menuTemp.menuClass + '-second').append('<li><a href=' + menuTemp2.url + '><i class="' + menuTemp2.iconClass + '"></i> ' + menuTemp2.menuName + '</a></li>')
                                }
                                $(".class-" + menuTemp.menuClass).append('</ul>')
                            }
                            
                            $('#side-menu').append('</li>')
                        }
                    }
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log("jqXHR = ", jqXHR)
                console.log("status = ", jqXHR.status)
                console.log("textStatus = ", textStatus)
    
                baseResponse = jqXHR.responseJSON
                swal(baseResponse.responseData.acknowledge, baseResponse.responseData.responseMessage, "error")
            }
        })
    }
})

async function determineGeneralUserRoleForAdd() {
    let currentRoles
    
    await $.ajax({
        url: '/api/v1/user/current-role',
            type: 'get',
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            success: function(result) {
                currentRoles = result.data
            }
    })
    
    if (currentRoles = null || currentRoles.length == 0 || (currentRoles.length > 0 && !currentRoles.includes("SUPER_ADMIN") && !currentRoles.includes("ADMIN")))
        $(".addButtonDataTable").css("display", "none")
}
