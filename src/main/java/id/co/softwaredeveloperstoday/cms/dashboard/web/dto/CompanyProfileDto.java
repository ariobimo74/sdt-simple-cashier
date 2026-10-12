package id.co.softwaredeveloperstoday.cms.dashboard.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyProfileDto extends RequestEditCompanyProfileDto {

    private String name;
    private String label;
    private Integer sortNumber;
    private String inputType;
    private Boolean htmlPreview;

}
