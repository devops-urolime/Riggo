package io.riggo.data.repositories;

import io.riggo.data.domain.Address;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends CrudRepository<Address, Long> {

    @Override
    Iterable<Address> findAll();

    Address findByExtSysId(String extSysId);
}
