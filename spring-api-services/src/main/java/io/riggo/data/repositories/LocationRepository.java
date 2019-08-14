package io.riggo.data.repositories;

import io.riggo.data.domain.Location;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends CrudRepository<Location, Integer> {

    @Query("select l from Location l where l.extSysId = :extSysId")
    Optional<Location> findByExtSysId(@Param("extSysId") String extSysId);

}
