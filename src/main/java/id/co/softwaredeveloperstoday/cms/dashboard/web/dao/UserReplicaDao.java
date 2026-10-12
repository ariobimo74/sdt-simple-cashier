package id.co.softwaredeveloperstoday.cms.dashboard.web.dao;

import id.co.softwaredeveloperstoday.cms.dashboard.web.model.entity.UserReplica;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserReplicaDao extends CrudRepository<UserReplica, Long> {

    List<UserReplica> findByUsernameIn(List<String> usernames);

}
