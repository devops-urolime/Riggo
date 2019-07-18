package io.riggo.data.services;

import io.riggo.data.domain.EquipmentType;
import io.riggo.data.exception.RiggoDataAccessException;
import io.riggo.data.repositories.EquipmentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EquipmentTypeService implements RiggoService {

    @Autowired
    private EquipmentTypeRepository equipmentTypeRepository;


    public EquipmentType save(EquipmentType et) {
        try {
            return equipmentTypeRepository.save(et);
        } catch (Exception e) {
            throw new RiggoDataAccessException(e);
        }
    }


    public Optional<EquipmentType> findById(Long eqId) {
        try {
            return equipmentTypeRepository.findById(eqId);
        } catch (Exception e) {
            throw new RiggoDataAccessException(e);
        }
    }


    public Optional<EquipmentType> findByExtSysId(String extSysId) {
        try {
            return equipmentTypeRepository.findByExtSysId(extSysId);
        } catch (Exception e) {
            throw new RiggoDataAccessException(e);
        }
    }
}
