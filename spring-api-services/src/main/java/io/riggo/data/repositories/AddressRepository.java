package io.riggo.data.repositories;

import io.riggo.data.domain.Address;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends CrudRepository<Address, Integer> {

    @Query("select a from Address a where a.extSysId = :extSysId")
    Optional<Address> findByExtSysId(@Param("extSysId") String extSysId);
}