package id.co.softwaredeveloperstoday.cms.dashboard.web.model.entity;

import id.co.softwaredeveloperstoday.cms.dashboard.web.model.CommonEntity;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.constant.IApplicationConstant;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.EOrderStatus;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.EPaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "order", schema = IApplicationConstant.SchemaName.TRANSACTION)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = IApplicationConstant.CommonValue.DefaultEntityWhereClause.ENTITY_WHERE_CLAUSE)
public class Order extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = IApplicationConstant.SchemaName.TRANSACTION + ".order_id_seq")
    @SequenceGenerator(name = IApplicationConstant.SchemaName.TRANSACTION + ".order_id_seq",
            sequenceName = IApplicationConstant.SchemaName.TRANSACTION +".order_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "totalPrice")
    private Double totalPrice;

    @Column(name = "cash_paid_by_customer")
    private Double cashPaidByCustomer;

    @Column(name = "change")
    private Double change;

    @Column(name = "payment_method", nullable = false)
    @Enumerated(EnumType.STRING)
    private EPaymentMethod paymentMethod;

    @Column(name = "attachment", columnDefinition = "bytea")
    private byte[] attachment;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private EOrderStatus status;

    @Column(name = "notes")
    private String notes;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "order", cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SELECT)
    private List<OrderItem> orderItems;

}
