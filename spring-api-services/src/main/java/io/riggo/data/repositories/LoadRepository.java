package io.riggo.data.repositories;

import io.riggo.data.domain.Load;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoadRepository extends CrudRepository<Load, Long> {

    @Query("select l from Load l where l.extSysId = :extSysId")
    Optional<Load> findByExtSysId(@Param("extSysId") String extSysId);
}
