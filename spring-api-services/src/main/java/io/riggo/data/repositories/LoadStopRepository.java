package io.riggo.data.repositories;

import io.riggo.data.domain.LoadStop;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoadStopRepository extends CrudRepository<LoadStop, Integer> {

    @Override
    Iterable<LoadStop> findAll();

    @Query("select l from LoadStop l where l.extSysId = :extSysId")
    Optional<LoadStop> findByExtSysId(@Param("extSysId") String extSysId);
}
