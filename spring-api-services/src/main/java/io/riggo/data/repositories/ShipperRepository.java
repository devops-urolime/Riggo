package io.riggo.data.repositories;

import io.riggo.data.domain.Shipper;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShipperRepository extends CrudRepository<Shipper, Integer> {

    @Query("SELECT s FROM Shipper s WHERE s.extSysId = :extSysId AND s.siteId = :siteId")
    Optional<Shipper> findByExtSysId(@Param("extSysId") String extSysId, @Param("siteId") Integer siteId);

    @Query("SELECT s FROM Shipper s JOIN ShipperUser su ON su.shipperId = s.id JOIN User u ON u.id = su.userId JOIN SiteUser siteUser ON siteUser.userId = u.id WHERE u.email = :email AND siteUser.siteId = :siteId")
    Optional<Shipper> findByEmailAndSiteId(@Param("email") String email, @Param("siteId") Integer siteId);
}
