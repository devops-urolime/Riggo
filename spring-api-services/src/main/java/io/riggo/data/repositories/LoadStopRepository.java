package io.riggo.data.repositories;

import io.riggo.data.domain.LoadStop;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoadStopRepository extends CrudRepository<LoadStop, Integer> {

    @Query("SELECT ls FROM LoadStop ls " +
            "JOIN Load l ON l.id = ls.loadId " +
            "WHERE ls.extSysId = :extSysId AND l.siteId = :siteId")
    Optional<LoadStop> findByExtSysId(@Param("extSysId") String extSysId, @Param("siteId") Integer siteId);
}
