package id.co.softwaredeveloperstoday.cms.dashboard.web.model.entity;

import id.co.softwaredeveloperstoday.cms.dashboard.web.util.constant.IApplicationConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user", schema = IApplicationConstant.SchemaName.MASTER_AUTHENTICATION)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserReplica {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = IApplicationConstant.SchemaName.MASTER_AUTHENTICATION + ".user_id_seq")
    @SequenceGenerator(name = IApplicationConstant.SchemaName.MASTER_AUTHENTICATION + ".user_id_seq",
            sequenceName = IApplicationConstant.SchemaName.MASTER_AUTHENTICATION +".user_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

}
