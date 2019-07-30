package io.riggo.data.services;

import io.riggo.data.domain.LoadStop;
import io.riggo.data.domain.LoadStopSummary;
import io.riggo.data.domain.ResourceType;
import io.riggo.data.exception.ResourceNotFoundException;
import io.riggo.data.exception.RiggoDataAccessException;
import io.riggo.data.repositories.LoadStopRepository;
import io.riggo.data.repositories.LoadStopSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoadStopService implements RiggoService {

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


    public LoadStop findById(Long eqId) throws ResourceNotFoundException {
        try {
            LoadStop eqt = loadStopRepository.findById(eqId).get();
            if (eqt == null) {
                throw new ResourceNotFoundException(ResourceType.SHIPPER, eqId);
            }
            return eqt;
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


    public Optional<List<LoadStopSummary>> findStopSummaryBySiteIdShipperId(Long siteId, Long shipperId) {
        try {
            return loadStopSummaryRepository.findStopSummaryBySiteIdShipperId(siteId, shipperId);
        } catch (Exception e) {
            throw new RiggoDataAccessException(e);
        }
    }
}
