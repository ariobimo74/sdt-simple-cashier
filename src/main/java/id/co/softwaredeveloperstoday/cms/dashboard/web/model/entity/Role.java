package id.co.softwaredeveloperstoday.cms.dashboard.web.model.entity;

import id.co.softwaredeveloperstoday.cms.dashboard.web.model.CommonEntity;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.constant.IApplicationConstant;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.ERoleName;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "role", schema = IApplicationConstant.SchemaName.MASTER_AUTHENTICATION)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = IApplicationConstant.CommonValue.DefaultEntityWhereClause.ENTITY_WHERE_CLAUSE)
public class Role extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = IApplicationConstant.SchemaName.MASTER_AUTHENTICATION + ".role_id_seq")
    @SequenceGenerator(name = IApplicationConstant.SchemaName.MASTER_AUTHENTICATION + ".role_id_seq",
            sequenceName = IApplicationConstant.SchemaName.MASTER_AUTHENTICATION + ".role_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "role_name", nullable = false)
    @Enumerated(EnumType.STRING)
    private ERoleName roleName;

    @Column(name = "description")
    private String description;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "role", cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SELECT)
    private List<UserRole> userRoles;
}
