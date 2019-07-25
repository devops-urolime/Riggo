package io.riggo.data.services;

import io.riggo.data.domain.Address;
import io.riggo.data.domain.ResourceType;
import io.riggo.data.exception.ResourceNotFoundException;
import io.riggo.data.exception.RiggoDataAccessException;
import io.riggo.data.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressService implements RiggoService {

    @Autowired
    private AddressRepository addressRepository;


    public Address save(Address addr) {
        try {
            return addressRepository.save(addr);
        } catch (Exception e) {
            throw new RiggoDataAccessException(e);
        }
    }


    public Address findById(Long eqId) {
        try {
            Address eqt = addressRepository.findById(eqId).get();
            if (eqt == null) {
                throw new ResourceNotFoundException(ResourceType.SHIPPER, eqId);
            }
            return eqt;
        } catch (Exception e) {
            throw new RiggoDataAccessException(e);
        }
    }


    public Optional<Address> findByExtSysId(String extSysId) {
        try {
            return addressRepository.findByExtSysId(extSysId);
        } catch (Exception e) {
            throw new RiggoDataAccessException(e);
        }
    }
}
