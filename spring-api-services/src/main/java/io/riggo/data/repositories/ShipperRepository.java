package io.riggo.data.repositories;

import io.riggo.data.domain.Shipper;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipperRepository extends CrudRepository<Shipper, Long> {

    @Override
    Iterable<Shipper> findAll();

    Shipper findByExtSysId(String extSysId);
}
