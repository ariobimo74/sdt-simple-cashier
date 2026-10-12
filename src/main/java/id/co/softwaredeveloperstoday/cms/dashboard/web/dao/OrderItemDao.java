package id.co.softwaredeveloperstoday.cms.dashboard.web.dao;

import id.co.softwaredeveloperstoday.cms.dashboard.web.model.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemDao extends JpaRepository<OrderItem, Long> {
}
