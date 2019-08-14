package io.riggo.data.services;

import io.riggo.data.domain.LoadStop;
import io.riggo.data.domain.LoadStopSummary;
import io.riggo.data.exception.RiggoDataAccessException;
import io.riggo.data.repositories.LoadStopRepository;
import io.riggo.data.repositories.LoadStopSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoadStopService{

    @Autowired
    private LoadStopRepository loadStopRepository;

    @Autowired
    private LoadStopSummaryRepository loadStopSummaryRepository;



    public LoadStop save(LoadStop et) {
        try {
            return loadStopRepository.save(et);
        } catch (Exception e) {
            throw new RiggoDataAccessException(e);
        }
    }


    public LoadStop findById(Integer eqId){
        try {
            return loadStopRepository.findById(eqId).get();
        } catch (Exception e) {
            throw new RiggoDataAccessException(e);
        }
    }


    public Optional<LoadStop> findByExtSysId(String extSysId) {
        try {
            return loadStopRepository.findByExtSysId(extSysId);
        } catch (Exception e) {
            throw new RiggoDataAccessException(e);
        }
    }


    public Optional<List<LoadStopSummary>> findStopSummaryBySiteIdShipperId(Integer siteId, Integer shipperId) {
        try {
            return loadStopSummaryRepository.findStopSummaryBySiteIdShipperId(siteId, shipperId);
        } catch (Exception e) {
            throw new RiggoDataAccessException(e);
        }
    }
}
