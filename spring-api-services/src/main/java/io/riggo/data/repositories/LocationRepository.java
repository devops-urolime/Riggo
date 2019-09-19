package io.riggo.data.repositories;

import io.riggo.data.domain.Location;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends CrudRepository<Location, Integer> {

    @Query("SELECT l FROM Location l " +
            "JOIN LoadStop ls ON ls.locationId = l.id " +
            "WHERE l.extSysId = :extSysId AND ls.extSysId = :loadStopExtSysId AND ls.id = :loadId")
    Optional<Location> findLocationByExtSysIdLoadStopExtSysIdLoadId(@Param("extSysId") String extSysId, @Param("loadStopExtSysId") String loadStopExtSysId,  @Param("loadId") Integer loadId);

}