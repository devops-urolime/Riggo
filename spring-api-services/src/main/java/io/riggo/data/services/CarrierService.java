package io.riggo.data.services;

import io.riggo.data.domain.Carrier;
import io.riggo.data.domain.ResourceType;
import io.riggo.data.exception.ResourceNotFoundException;
import io.riggo.data.exception.RiggoDataAccessException;
import io.riggo.data.repositories.CarrierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarrierService implements RiggoService {

    @Autowired
    private CarrierRepository carrierRepository;


    public Carrier save(Carrier et) throws RiggoDataAccessException {
        try {
            return carrierRepository.save(et);
        } catch (Exception e) {
            throw new RiggoDataAccessException("Data Access Exception occurred", e);
        }
    }


    public Carrier findById(Long eqId) throws ResourceNotFoundException, RiggoDataAccessException {
        try {
            Carrier eqt = carrierRepository.findById(eqId).get();
            if (eqt == null) {
                throw new ResourceNotFoundException(ResourceType.SHIPPER, eqId);
            }
            return eqt;
        } catch (Exception e) {
            throw new RiggoDataAccessException("Data Access Exception occurred", e);
        }
    }


    public Carrier findByExtSysId(String extSysId) {
        try {
            return carrierRepository.findByExtSysId(extSysId);
        } catch (Exception e) {
            throw new RiggoDataAccessException("Data Access Exception occurred", e);
        }
    }
}
