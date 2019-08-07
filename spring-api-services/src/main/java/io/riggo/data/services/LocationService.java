package io.riggo.data.services;

import io.riggo.data.domain.Location;
import io.riggo.data.domain.ResourceType;
import io.riggo.data.exception.ResourceNotFoundException;
import io.riggo.data.exception.RiggoDataAccessException;
import io.riggo.data.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;


    public Location save(Location et) {
        try {
            return locationRepository.save(et);
        } catch (Exception e) {
            throw new RiggoDataAccessException(e);
        }
    }


    public Location findById(Integer eqId) {
        try {
            Location eqt = locationRepository.findById(eqId).get();
            if (eqt == null) {
                throw new ResourceNotFoundException(ResourceType.SHIPPER, eqId);
            }
            return eqt;
        } catch (Exception e) {
            throw new RiggoDataAccessException(e);
        }
    }


    public Optional<Location> findByExtSysId(String extSysId) {
        try {
            return locationRepository.findByExtSysId(extSysId);
        } catch (Exception e) {
            throw new RiggoDataAccessException(e);
        }
    }
}
