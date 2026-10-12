package id.co.softwaredeveloperstoday.cms.dashboard.web.model.entity;

import id.co.softwaredeveloperstoday.cms.dashboard.web.model.CommonAudit;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.constant.IApplicationConstant;
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
@Table(name = "category", schema = IApplicationConstant.SchemaName.MASTER_CATALOG)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category extends CommonAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = IApplicationConstant.SchemaName.MASTER_CATALOG + ".category_id_seq")
    @SequenceGenerator(name = IApplicationConstant.SchemaName.MASTER_CATALOG + ".category_id_seq",
            sequenceName = IApplicationConstant.SchemaName.MASTER_CATALOG +".category_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "category", cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SELECT)
    private List<Product> products;

}
