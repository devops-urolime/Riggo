package io.riggo.data.repositories;

import io.riggo.data.domain.EquipmentType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentTypeRepository extends CrudRepository<EquipmentType, Long> {

    @Override
    Iterable<EquipmentType> findAll();

    EquipmentType findByExtSysId(String extSysId);
}
