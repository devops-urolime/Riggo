package io.riggo.data.services;

import io.riggo.data.domain.ResourceType;
import io.riggo.data.domain.Trucker;
import io.riggo.data.exception.ResourceNotFoundException;
import io.riggo.data.exception.RiggoDataAccessException;
import io.riggo.data.repositories.TruckerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TruckerService implements RiggoService {

    @Autowired
    private TruckerRepository truckerRepository;


    public Trucker save(Trucker et) throws RiggoDataAccessException {
        try {
            return truckerRepository.save(et);
        } catch (Exception e) {
            throw new RiggoDataAccessException("Data Access Exception occurred", e);
        }
    }


    public Trucker findById(Long eqId) throws ResourceNotFoundException, RiggoDataAccessException {
        try {
            Trucker eqt = truckerRepository.findById(eqId).get();
            if (eqt == null) {
                throw new ResourceNotFoundException(ResourceType.SHIPPER, eqId);
            }
            return eqt;
        } catch (Exception e) {
            throw new RiggoDataAccessException("Data Access Exception occurred", e);
        }
    }


    public Trucker findByExtSysId(String extSysId) {
        try {
            return truckerRepository.findByExtSysId(extSysId);
        } catch (Exception e) {
            throw new RiggoDataAccessException("Data Access Exception occurred", e);
        }
    }
}
