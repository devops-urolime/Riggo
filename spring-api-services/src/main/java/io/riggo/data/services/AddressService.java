package io.riggo.data.services;

import io.riggo.data.domain.Address;
import io.riggo.data.exception.RiggoDataAccessException;
import io.riggo.data.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;


    public Address save(Address addr) {
        try {
            return addressRepository.save(addr);
        } catch (Exception e) {
            throw new RiggoDataAccessException(e);
        }
    }


    public Optional<Address> findById(Integer id) {
        try {
            return addressRepository.findById(id);
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
