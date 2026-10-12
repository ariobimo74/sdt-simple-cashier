package id.co.softwaredeveloperstoday.cms.dashboard.web.controller;

import id.co.softwaredeveloperstoday.cms.dashboard.web.util.constant.IApplicationConstant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderController {

    @GetMapping(IApplicationConstant.RestVersion.Order.PRODUCT)
    public String requestOrder() {
        return "order/table";
    }

    @GetMapping(IApplicationConstant.RestVersion.Order.PAYMENT_ID)
    public String paymentOrder() {
        return "order/detail";
    }

    @GetMapping(IApplicationConstant.RestVersion.Order.PRODUCT + IApplicationConstant.RestVersion.Order.HISTORY)
    public String orderHistory() {
        return "order/history";
    }

    @GetMapping(IApplicationConstant.RestVersion.Order.PRODUCT + IApplicationConstant.RestVersion.Order.DETAIL_ID)
    public String orderDetail() {return paymentOrder();}

}
