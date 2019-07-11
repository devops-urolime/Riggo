package io.riggo.data.services;

import io.riggo.data.domain.EquipmentType;
import io.riggo.data.domain.ResourceType;
import io.riggo.data.exception.ResourceNotFoundException;
import io.riggo.data.exception.RiggoDataAccessException;
import io.riggo.data.repositories.EquipmentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EquipmentTypeService implements RiggoService {

    @Autowired
    private EquipmentTypeRepository equipmentTypeRepository;


    public EquipmentType save(EquipmentType et) throws RiggoDataAccessException {
        try {
            return equipmentTypeRepository.save(et);
        } catch (Exception e) {
            throw new RiggoDataAccessException("Data Access Exception occurred", e);
        }
    }


    public EquipmentType findById(Long eqId) throws ResourceNotFoundException, RiggoDataAccessException {
        try {
            EquipmentType eqt = equipmentTypeRepository.findById(eqId).get();
            if (eqt == null) {
                throw new ResourceNotFoundException(ResourceType.SHIPPER, eqId);
            }
            return eqt;
        } catch (Exception e) {
            throw new RiggoDataAccessException("Data Access Exception occurred", e);
        }
    }


    public EquipmentType findByExtSysId(String extSysId) {
        try {
            return equipmentTypeRepository.findByExtSysId(extSysId);
        } catch (Exception e) {
            throw new RiggoDataAccessException("Data Access Exception occurred", e);
        }
    }
}
