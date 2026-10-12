package id.co.softwaredeveloperstoday.cms.dashboard.web.dto;

import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.EPaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestPaymentDto {

    @NotNull
    private Long orderId;
    @NotNull
    private Double paidByCustomer;
    @NotNull
    private EPaymentMethod paymentMethod;
    private byte[] attachment;

    private String notes;
    private Boolean sendMail;
    private String emailAddress;

}
