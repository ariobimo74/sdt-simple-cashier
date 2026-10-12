package id.co.softwaredeveloperstoday.cms.dashboard.web.controller;

import id.co.softwaredeveloperstoday.cms.dashboard.web.util.constant.IApplicationConstant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductController {

    @GetMapping("/product")
    public String productList() {
        return "product/table";
    }

    @GetMapping(IApplicationConstant.RestVersion.Product.ADD)
    public String addProduct() {
        return viewDetailProduct();
    }

    @GetMapping(IApplicationConstant.RestVersion.Product.UPDATE_ITEM_ID)
    public String editProduct() {
        return viewDetailProduct();
    }

    @GetMapping(IApplicationConstant.RestVersion.Product.DETAIL_ITEM_ID)
    public String viewDetailProduct() {
        return "product/detail";
    }

    @GetMapping("/category")
    public String categoryList() {
        return "product/category";
    }

}
