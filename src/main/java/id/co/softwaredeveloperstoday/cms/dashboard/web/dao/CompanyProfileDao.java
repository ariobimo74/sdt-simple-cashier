package id.co.softwaredeveloperstoday.cms.dashboard.web.dao;

import id.co.softwaredeveloperstoday.cms.dashboard.web.model.entity.CompanyProfile;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyProfileDao extends CrudRepository<CompanyProfile, Long> {

    Optional<CompanyProfile> findFirstByName(String name);

    List<CompanyProfile> findAllByOrderBySortNumberAsc();

}
