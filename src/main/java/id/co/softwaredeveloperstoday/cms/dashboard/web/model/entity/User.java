package id.co.softwaredeveloperstoday.cms.dashboard.web.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import id.co.softwaredeveloperstoday.cms.dashboard.web.model.CommonEntity;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.constant.IApplicationConstant;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "user", schema = IApplicationConstant.SchemaName.MASTER_AUTHENTICATION)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = IApplicationConstant.CommonValue.DefaultEntityWhereClause.ENTITY_WHERE_CLAUSE)
public class User extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = IApplicationConstant.SchemaName.MASTER_AUTHENTICATION + ".user_id_seq")
    @SequenceGenerator(name = IApplicationConstant.SchemaName.MASTER_AUTHENTICATION + ".user_id_seq",
            sequenceName = IApplicationConstant.SchemaName.MASTER_AUTHENTICATION +".user_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "passwordmodified_date")
    private Date passwordmodifiedDate;

    @Column(name = "passwordmodified_by")
    private String passwordmodifiedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "login_date")
    private Date loginDate;

    @Column(name = "login_ip")
    private String loginIp;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "lockout_date")
    private Date lockoutDate;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SELECT)
    private List<UserRole> userRoles;

}
