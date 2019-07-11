package io.riggo.data.repositories;

import io.riggo.data.domain.LoadStop;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoadStopRepository extends CrudRepository<LoadStop, Long> {

    @Override
    Iterable<LoadStop> findAll();

    LoadStop findByExtSysId(String extSysId);
}
