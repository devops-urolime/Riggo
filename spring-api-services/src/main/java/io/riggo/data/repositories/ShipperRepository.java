package io.riggo.data.repositories;

import io.riggo.data.domain.Shipper;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShipperRepository extends CrudRepository<Shipper, Integer> {

    @Override
    Iterable<Shipper> findAll();

    @Query("select s from Shipper s where s.extSysId = :extSysId")
    Optional<Shipper> findByExtSysId(@Param("extSysId") String extSysId);
}
