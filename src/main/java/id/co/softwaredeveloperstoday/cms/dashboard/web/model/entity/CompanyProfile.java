package id.co.softwaredeveloperstoday.cms.dashboard.web.model.entity;

import id.co.softwaredeveloperstoday.cms.dashboard.web.model.CommonAudit;
import id.co.softwaredeveloperstoday.cms.dashboard.web.model.CommonEntity;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.constant.IApplicationConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "company_profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = IApplicationConstant.CommonValue.DefaultEntityWhereClause.ENTITY_WHERE_CLAUSE)
public class CompanyProfile extends CommonAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =  "company_profile_id_seq")
    @SequenceGenerator(name =  "company_profile_id_seq",
            sequenceName = "company_profile_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "label")
    private String label;

    @Column(name = "sort_number")
    private Integer sortNumber;

    @Column(name = "content")
    private String content;

    @Column(name = "input_type")
    private String inputType;

    @Column(name = "html_preview")
    private Boolean htmlPreview;

}
