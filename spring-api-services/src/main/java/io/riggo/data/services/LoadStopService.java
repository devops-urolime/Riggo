package io.riggo.data.services;

import io.riggo.data.domain.LoadStop;
import io.riggo.data.domain.ResourceType;
import io.riggo.data.exception.ResourceNotFoundException;
import io.riggo.data.exception.RiggoDataAccessException;
import io.riggo.data.repositories.LoadStopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoadStopService implements RiggoService {

    @Autowired
    private LoadStopRepository loadStopRepository;


    public LoadStop save(LoadStop et) throws RiggoDataAccessException {
        try {
            return loadStopRepository.save(et);
        } catch (Exception e) {
            throw new RiggoDataAccessException("Data Access Exception occurred", e);
        }
    }


    public LoadStop findById(Long eqId) throws ResourceNotFoundException, RiggoDataAccessException {
        try {
            LoadStop eqt = loadStopRepository.findById(eqId).get();
            if (eqt == null) {
                throw new ResourceNotFoundException(ResourceType.SHIPPER, eqId);
            }
            return eqt;
        } catch (Exception e) {
            throw new RiggoDataAccessException("Data Access Exception occurred", e);
        }
    }


    public LoadStop findByExtSysId(String extSysId) {
        try {
            return loadStopRepository.findByExtSysId(extSysId);
        } catch (Exception e) {
            throw new RiggoDataAccessException("Data Access Exception occurred", e);
        }
    }
}
