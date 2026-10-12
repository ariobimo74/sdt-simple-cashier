package id.co.softwaredeveloperstoday.cms.dashboard.web.model.entity;

import id.co.softwaredeveloperstoday.cms.dashboard.web.model.CommonEntity;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.constant.IApplicationConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "order_item", schema = IApplicationConstant.SchemaName.TRANSACTION)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = IApplicationConstant.CommonValue.DefaultEntityWhereClause.ENTITY_WHERE_CLAUSE)
public class OrderItem extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = IApplicationConstant.SchemaName.TRANSACTION + ".order_item_id_seq")
    @SequenceGenerator(name = IApplicationConstant.SchemaName.TRANSACTION + ".order_item_id_seq",
            sequenceName = IApplicationConstant.SchemaName.TRANSACTION +".order_item_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name="order_id", referencedColumnName = "id")
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name="product_id", referencedColumnName = "id")
    private Product product;

    @Column(name = "total_item")
    private Integer totalItem;

    @Column(name = "current_item_price")
    private Double currentItemPrice;

}
