package id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ETransactionHistorySortBy {

    ORDER_NUMBER("orderNumber"), PAYMENT_METHOD("paymentMethod"), STATUS("status"), CREATED_DATE("createdDate"), MODIFIED_DATE("modifiedDate");

    private final String name;
}
