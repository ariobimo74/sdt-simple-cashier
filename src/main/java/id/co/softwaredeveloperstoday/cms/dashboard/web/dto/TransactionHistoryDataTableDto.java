package id.co.softwaredeveloperstoday.cms.dashboard.web.dto;

import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.EOrderStatus;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.EPaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionHistoryDataTableDto {

    private Long id;
    private String orderNumber;
    private EPaymentMethod paymentMethod;
    private EOrderStatus status;
    private String htmlStatus;
    private String createdDate;
    private String modifiedDate;
    private ActionDataTableIdentityDto actionsId;

}
