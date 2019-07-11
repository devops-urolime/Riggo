package io.riggo.data.services;

import io.riggo.data.domain.ResourceType;
import io.riggo.data.domain.Shipper;
import io.riggo.data.exception.ResourceNotFoundException;
import io.riggo.data.exception.RiggoDataAccessException;
import io.riggo.data.repositories.ShipperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShipperService implements RiggoService {

    @Autowired
    private ShipperRepository shipperRepository;


    public Shipper save(Shipper shipper) throws RiggoDataAccessException {
        try {
            return shipperRepository.save(shipper);
        } catch (Exception e) {
            throw new RiggoDataAccessException("Data Access Exception occurred", e);
        }
    }


    public Shipper findById(Long shipperId) throws ResourceNotFoundException, RiggoDataAccessException {
        try {
            Shipper shipper = shipperRepository.findById(shipperId).get();
            if (shipper == null) {
                throw new ResourceNotFoundException(ResourceType.SHIPPER, shipperId);
            }
            return shipper;
        } catch (Exception e) {
            throw new RiggoDataAccessException("Data Access Exception occurred", e);
        }
    }


    public Shipper findByExtSysId(String extSysId) {
        try {
            return shipperRepository.findByExtSysId(extSysId);
        } catch (Exception e) {
            throw new RiggoDataAccessException("Data Access Exception occurred", e);
        }
    }
}
