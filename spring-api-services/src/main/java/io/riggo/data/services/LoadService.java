package io.riggo.data.services;

import io.riggo.data.domain.Load;
import io.riggo.data.exception.RiggoDataAccessException;
import io.riggo.data.repositories.LoadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoadService {

    @Autowired
    private LoadRepository loadRepository;


    public Optional<Load> findByExtSysId(String extSysId) {
        try {
            return loadRepository.findByExtSysId(extSysId);
        } catch (Exception e) {
            throw new RiggoDataAccessException("Data Access Exception occurred", e);
        }
    }

    public Optional<Load> findById(Integer id) {
        try {
            return loadRepository.findById(id);
        } catch (Exception e) {
            throw new RiggoDataAccessException("Data Access Exception occurred", e);
        }
    }

    public Load save(Load load) {
        try {
            return loadRepository.save(load);
        } catch (Exception e) {
            throw new RiggoDataAccessException("Could not save Load data", e);
        }
    }

}