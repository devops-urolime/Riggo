package io.riggo.data.services;

import io.riggo.data.domain.Shipper;
import io.riggo.data.exception.ResourceNotFoundException;
import io.riggo.data.exception.RiggoDataAccessException;
import io.riggo.data.repositories.ShipperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShipperService implements RiggoService {

    @Autowired
    private ShipperRepository shipperRepository;


    public Shipper save(Shipper shipper) {
        try {
            return shipperRepository.save(shipper);
        } catch (Exception e) {
            throw new RiggoDataAccessException(e);
        }
    }


    public Optional<Shipper> findById(Long shipperId) throws ResourceNotFoundException {
        try {
            return shipperRepository.findById(shipperId);
        } catch (Exception e) {
            throw new RiggoDataAccessException(e);
        }
    }


    public Optional<Shipper> findByExtSysId(String extSysId) {
        try {
            return shipperRepository.findByExtSysId(extSysId);
        } catch (Exception e) {
            throw new RiggoDataAccessException(e);
        }
    }
}
