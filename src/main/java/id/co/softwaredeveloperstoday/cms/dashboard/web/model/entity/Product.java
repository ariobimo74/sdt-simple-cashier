package id.co.softwaredeveloperstoday.cms.dashboard.web.model.entity;

import id.co.softwaredeveloperstoday.cms.dashboard.web.model.CommonAudit;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.constant.IApplicationConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "product", schema = IApplicationConstant.SchemaName.MASTER_CATALOG)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = IApplicationConstant.CommonValue.DefaultEntityWhereClause.ENTITY_WHERE_CLAUSE)
public class Product extends CommonAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = IApplicationConstant.SchemaName.MASTER_CATALOG + ".product_id_seq")
    @SequenceGenerator(name = IApplicationConstant.SchemaName.MASTER_CATALOG + ".product_id_seq",
            sequenceName = IApplicationConstant.SchemaName.MASTER_CATALOG +".product_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name="category_id", referencedColumnName = "id")
    private Category category;

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "stock")
    private Long stock;

    @Column(name = "unit")
    private String unit;

    @Column(name = "price_per_unit")
    private Double pricePerUnit;

    @Column(name = "image", columnDefinition = "bytea")
    private byte[] image;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "product", cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SELECT)
    private List<OrderItem> orderItems;

}
