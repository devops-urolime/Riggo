package io.riggo.data.repositories;

import io.riggo.data.domain.Trucker;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TruckerRepository extends CrudRepository<Trucker, Long> {

    @Override
    Iterable<Trucker> findAll();

    Trucker findByExtSysId(String extSysId);
}
