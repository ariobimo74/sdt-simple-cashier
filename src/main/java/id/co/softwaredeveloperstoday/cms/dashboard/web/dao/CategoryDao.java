package id.co.softwaredeveloperstoday.cms.dashboard.web.dao;

import id.co.softwaredeveloperstoday.cms.dashboard.web.model.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryDao extends CrudRepository<Category, Long> {

    List<Category> findAllByIsDeleteOrderByNameAsc(Boolean isDelete);

    Page<Category> findByNameContainingIgnoreCaseOrderByNameAsc(String name, Pageable pageable);

}
