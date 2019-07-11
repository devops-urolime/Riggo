package io.riggo.data.repositories;

import io.riggo.data.domain.Location;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends CrudRepository<Location, Long> {

    @Override
    Iterable<Location> findAll();

    Location findByExtSysId(String extSysId);
}
