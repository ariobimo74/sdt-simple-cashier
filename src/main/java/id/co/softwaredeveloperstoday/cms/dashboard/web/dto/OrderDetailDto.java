package id.co.softwaredeveloperstoday.cms.dashboard.web.dto;

import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.EOrderStatus;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.EPaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDto extends CalculateOrderDto {

    private Long id;
    private String orderNumber;

    private Double cashPaidByCustomer;
    private String cashPaidByCustomerWithCurrency;

    private Double change;
    private String changeWithCurrency;

    private EPaymentMethod paymentMethod;
    private EOrderStatus status;
    private String notes;

    private byte[] attachment;
    private String encodedAttachment;
    private List<EPaymentMethod> availablePaymentMethods = Arrays.stream(EPaymentMethod.values()).collect(Collectors.toList());
    
    private Date createdDate;
    private String createdBy;

}
