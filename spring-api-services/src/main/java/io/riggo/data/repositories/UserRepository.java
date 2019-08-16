package io.riggo.data.repositories;

import io.riggo.data.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    @Query("SELECT u FROM User u JOIN SiteUser su ON su.userId = u.id JOIN Site s ON s.id = su.siteId WHERE u.email = :email AND s.id = :siteId")
    Optional<User> findByEmailAndSiteId(@Param("email")String email, @Param("siteId") Integer siteId);
}
