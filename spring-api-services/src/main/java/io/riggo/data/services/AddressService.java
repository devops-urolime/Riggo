package io.riggo.data.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.riggo.data.domain.Address;
import io.riggo.data.domain.ResourceType;
import io.riggo.data.exception.ResourceNotFoundException;
import io.riggo.data.exception.RiggoDataAccessException;
import io.riggo.data.helpers.AddressDTOLoad;
import io.riggo.data.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService implements RiggoService {

    @Autowired
    private AddressRepository addressRepository;


    public Address save(Address addr) throws RiggoDataAccessException {
        try {
            return addressRepository.save(addr);
        } catch (Exception e) {
            throw new RiggoDataAccessException("Data Access Exception occurred", e);
        }
    }

    public Address saveDTO(AddressDTOLoad addressDTOLoad) throws RiggoDataAccessException {
        try {
            Address addr = new ObjectMapper().convertValue(addressDTOLoad, Address.class);
            return addressRepository.save(addr);
        } catch (Exception e) {
            throw new RiggoDataAccessException("Data Access Exception occurred", e);
        }
    }


    public Address findById(Long eqId) throws ResourceNotFoundException, RiggoDataAccessException {
        try {
            Address eqt = addressRepository.findById(eqId).get();
            if (eqt == null) {
                throw new ResourceNotFoundException(ResourceType.SHIPPER, eqId);
            }
            return eqt;
        } catch (Exception e) {
            throw new RiggoDataAccessException("Data Access Exception occurred", e);
        }
    }


    public Address findByExtSysId(String extSysId) {
        try {
            return addressRepository.findByExtSysId(extSysId);
        } catch (Exception e) {
            throw new RiggoDataAccessException("Data Access Exception occurred", e);
        }
    }
}
