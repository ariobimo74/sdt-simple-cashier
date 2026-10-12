package id.co.softwaredeveloperstoday.cms.dashboard.web.service;

import id.co.softwaredeveloperstoday.cms.dashboard.web.dto.*;
import id.co.softwaredeveloperstoday.cms.dashboard.web.util.enumeration.EMemberLevel;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface CommonValueService {

    List<ResponseCommonEnumDto> getGenders();

    ResponseCommonEnumDto getDefaultMember(EMemberLevel memberLevel);

    List<ResponseCommonEnumDto> getMembers();

    List<String> getRecommendedUsername(RequestRecommendedUsernameDto recommendedUsernameDto);

    DashboardIncomeDto getIncomeForDashboard();

    List<CompanyProfileDto> getAllCompanyProfiles();

    CompanyProfileDto patchCompanyProfile(Authentication authentication, RequestEditCompanyProfileDto profileDto);

}
