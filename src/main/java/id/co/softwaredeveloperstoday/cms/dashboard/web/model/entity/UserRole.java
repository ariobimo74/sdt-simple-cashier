package id.co.softwaredeveloperstoday.cms.dashboard.web.model.entity;

import id.co.softwaredeveloperstoday.cms.dashboard.web.model.CommonEntity;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.constant.IApplicationConstant;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "user_role", schema = IApplicationConstant.SchemaName.MASTER_AUTHENTICATION)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = IApplicationConstant.CommonValue.DefaultEntityWhereClause.ENTITY_WHERE_CLAUSE)
public class UserRole extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = IApplicationConstant.SchemaName.MASTER_AUTHENTICATION + ".user_role_id_seq")
    @SequenceGenerator(name = IApplicationConstant.SchemaName.MASTER_AUTHENTICATION + ".user_role_id_seq",
            sequenceName = IApplicationConstant.SchemaName.MASTER_AUTHENTICATION + ".user_role_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name="role_id", referencedColumnName = "id")
    private Role role;
}
