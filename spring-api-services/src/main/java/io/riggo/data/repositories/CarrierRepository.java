package io.riggo.data.repositories;

import io.riggo.data.domain.Carrier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarrierRepository extends CrudRepository<Carrier, Long> {

    @Override
    Iterable<Carrier> findAll();

    Carrier findByExtSysId(String extSysId);
}
