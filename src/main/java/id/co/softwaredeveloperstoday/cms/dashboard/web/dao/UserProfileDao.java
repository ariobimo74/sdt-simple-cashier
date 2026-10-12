package id.co.softwaredeveloperstoday.cms.dashboard.web.dao;

import id.co.softwaredeveloperstoday.cms.dashboard.web.model.entity.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileDao extends JpaRepository<UserProfile, Long> {

    Optional<UserProfile> findById(Long id);

    Page<UserProfile> findAll(Specification<UserProfile> userProfileSpecification, Pageable pageable);

    UserProfile findByUserId(Long userId);

}
