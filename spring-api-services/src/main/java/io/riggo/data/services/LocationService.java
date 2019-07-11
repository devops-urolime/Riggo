package io.riggo.data.services;

import io.riggo.data.domain.Location;
import io.riggo.data.domain.ResourceType;
import io.riggo.data.exception.ResourceNotFoundException;
import io.riggo.data.exception.RiggoDataAccessException;
import io.riggo.data.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationService implements RiggoService {

    @Autowired
    private LocationRepository locationRepository;


    public Location save(Location et) throws RiggoDataAccessException {
        try {
            return locationRepository.save(et);
        } catch (Exception e) {
            throw new RiggoDataAccessException("Data Access Exception occurred", e);
        }
    }


    public Location findById(Long eqId) throws ResourceNotFoundException, RiggoDataAccessException {
        try {
            Location eqt = locationRepository.findById(eqId).get();
            if (eqt == null) {
                throw new ResourceNotFoundException(ResourceType.SHIPPER, eqId);
            }
            return eqt;
        } catch (Exception e) {
            throw new RiggoDataAccessException("Data Access Exception occurred", e);
        }
    }


    public Location findByExtSysId(String extSysId) {
        try {
            return locationRepository.findByExtSysId(extSysId);
        } catch (Exception e) {
            throw new RiggoDataAccessException("Data Access Exception occurred", e);
        }
    }
}
