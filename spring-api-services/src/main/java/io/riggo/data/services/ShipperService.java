package io.riggo.data.services;

import io.riggo.data.domain.Shipper;
import io.riggo.data.exception.RiggoDataAccessException;
import io.riggo.data.repositories.ShipperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShipperService {

    @Autowired
    private ShipperRepository shipperRepository;


    public Shipper save(Shipper shipper) {
        try {
            return shipperRepository.save(shipper);
        } catch (Exception e) {
            throw new RiggoDataAccessException(e);
        }
    }


    public Optional<Shipper> findByEmailAndSiteId(String username, Integer siteId){
        try{
            return shipperRepository.findByEmailAndSiteId(username, siteId);
        } catch (Exception e) {
            throw new RiggoDataAccessException(e);
        }
    }

    public Optional<Shipper> findByExtSysId(String extSysId, Integer siteId) {
        try {
            return shipperRepository.findByExtSysId(extSysId, siteId);
        } catch (Exception e) {
            throw new RiggoDataAccessException(e);
        }
    }
}
