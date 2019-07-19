package io.riggo.data.services;

import io.riggo.data.domain.LoadLineItem;
import io.riggo.data.exception.RiggoDataAccessException;
import io.riggo.data.repositories.LoadLineItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoadLineItemService {

    @Autowired
    private LoadLineItemRepository loadLineItemRepository;


    public LoadLineItem save(LoadLineItem loadLineItem) {
        try {
            return loadLineItemRepository.save(loadLineItem);
        } catch (Exception e) {
            throw new RiggoDataAccessException(e);
        }
    }


    public Optional<LoadLineItem> findById(Long eqId) {
        try {
            return loadLineItemRepository.findById(eqId);
        } catch (Exception e) {
            throw new RiggoDataAccessException(e);
        }
    }


    public Optional<List<LoadLineItem>> findByLoadId(Integer loadId) {
        try {
            return loadLineItemRepository.findByLoadId(loadId);
        } catch (Exception e) {
            throw new RiggoDataAccessException(e);
        }
    }
}
