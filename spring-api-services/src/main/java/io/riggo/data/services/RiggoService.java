package io.riggo.data.services;

import io.riggo.data.exception.ResourceNotFoundException;
import io.riggo.data.exception.RiggoDataAccessException;

public interface RiggoService {

    Object findById(Long id) throws ResourceNotFoundException, RiggoDataAccessException;

    Object findByExtSysId(String extSysId);
}
