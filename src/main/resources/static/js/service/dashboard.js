$(document).ready(function() {
    generateDashboard()
})

async function generateDashboard() {
    await $.ajax({
        url: '/api/v1/value/dashboard',
        type: 'get',
        contentType: 'application/json; charset=utf-8',
        success: function(result) {
            baseResponse = result    
            if (baseResponse.responseData.responseCode == 200) {
                const dashboardData = baseResponse.data
                $('#totalSale6Months').text(dashboardData.totalSalesLast6Month)
                $('#totalSaleToday').text(dashboardData.totalSalesToday)
                $('#currentUserSale').text(dashboardData.currentUserSalesToday)
                $('#currentUserSaleInfo').text("Today's User Sales : " + dashboardData.userName)

                $('#todayProductSalesTotal').text("Total : " + dashboardData.totalTodayProductSale)
                $('#monthlyProductSalesTotal').text("Total : " + dashboardData.totalProductSaleInAMounth)

                generateDataTable(dashboardData.todaySaleProducts, dashboardData.monthlySaleProducts)
            }
            else swal(result.responseData.acknowledge, result.responseData.responseMessage, "error")
        }, 
        error: function(jqXHR, textStatus, errorThrown) {
            console.log("jqXHR = ", jqXHR)
            console.log("status = ", jqXHR.status)
            console.log("textStatus = ", textStatus)

            baseResponse = jqXHR.responseJSON
            
            if (parseInt(jqXHR.status) == parseInt(404)) window.location.href = "/error/404"
            swal(baseResponse.responseData.acknowledge, baseResponse.responseData.responseMessage, "error")
        }
    })
}

async function generateDataTable(todaySaleProducts, monthlySaleProducts) {
    await $('#product-sale-today-dataTables').DataTable({
        ordering: false,
        responsive: true,
        processing: false,
        serverSide: false,
        data : todaySaleProducts,
        columns: [
            {
                data: null,
                render: function (data, type, row, meta) {
                    return meta.row + meta.settings._iDisplayStart + 1;
                },
                orderable: false
            },
            { data: 'name' },
            { data: 'totalItem' },
        ]
    })

    await $('#product-sale-month-dataTables').DataTable({
        ordering: false,
        responsive: true,
        processing: false,
        serverSide: false,
        data : monthlySaleProducts,
        columns: [
            {
                data: null,
                render: function (data, type, row, meta) {
                    return meta.row + meta.settings._iDisplayStart + 1;
                },
                orderable: false
            },
            { data: 'name' },
            { data: 'totalItem' },
        ]
    })
}
