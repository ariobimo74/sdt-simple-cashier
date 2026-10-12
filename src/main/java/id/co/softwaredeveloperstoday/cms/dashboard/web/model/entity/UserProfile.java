package id.co.softwaredeveloperstoday.cms.dashboard.web.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import id.co.softwaredeveloperstoday.cms.dashboard.web.model.CommonAudit;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.constant.IApplicationConstant;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.EGenderType;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.EMaritalStatus;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.EMemberLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_profile", schema = IApplicationConstant.SchemaName.MASTER_AUTHENTICATION)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = IApplicationConstant.CommonValue.DefaultEntityWhereClause.ENTITY_WHERE_CLAUSE)
public class UserProfile extends CommonAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = IApplicationConstant.SchemaName.MASTER_AUTHENTICATION + ".user_profile_id_seq")
    @SequenceGenerator(name = IApplicationConstant.SchemaName.MASTER_AUTHENTICATION + ".user_profile_id_seq",
            sequenceName = IApplicationConstant.SchemaName.MASTER_AUTHENTICATION + ".user_profile_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "gender")
    @Enumerated(value = EnumType.STRING)
    private EGenderType gender;

    @Column(name = "email")
    private String email;

    @Column(name = "is_email_verified")
    private Boolean isEmailVerified;

    @Column(name = "mobile_phone_number")
    private String mobilePhoneNumber;

    @Column(name = "is_mobile_phone_number_verified")
    private Boolean isMobilePhoneNumberVerified;

    @Column(name = "photo_url")
    private String photoUrl;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private User user;

    @Column(name = "address")
    private String address;

    @Column(name = "village")
    private String village;

    @Column(name = "district")
    private String district;

    @Column(name = "country")
    private String country = "Indonesia";

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @Column(name = "place_of_birth")
    private String placeOfBirth;

    @Column(name = "name_according_to_id_card")
    private String nameAccordingToIdCard;

    @Column(name = "id_card_number")
    private String idCardNumber;

    @Column(name = "is_verification_user")
    private Boolean isVerifiedUser;

    @Column(name = "job")
    private String job;

    @Column(name = "marital_status")
    @Enumerated(EnumType.STRING)
    private EMaritalStatus maritalStatus;

    @Column(name = "member_level")
    @Enumerated(EnumType.STRING)
    private EMemberLevel memberLevel = EMemberLevel.REGULAR;

}
