package io.riggo.data.repositories;

import io.riggo.data.domain.EquipmentType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EquipmentTypeRepository extends CrudRepository<EquipmentType, Long> {

    @Override
    Iterable<EquipmentType> findAll();

    @Query("select et from EquipmentType et where et.extSysId = :extSysId")
    Optional<EquipmentType> findByExtSysId(String extSysId);
}
