package io.riggo.data.services;

import io.riggo.data.domain.LoadPipeline;
import io.riggo.data.exception.RiggoDataAccessException;
import io.riggo.data.repositories.LoadPipelineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoadPipelineService {

    @Autowired
    private LoadPipelineRepository loadPipelineRepository;

    public Optional<List<LoadPipeline>> findPipelineSummaryBySiteIdShipperId(Long siteId, Long shipperId) {
        try{
            return loadPipelineRepository.findPipelineSummaryBySiteIdShipperId(siteId, shipperId);
        }catch (Exception e){
            throw new RiggoDataAccessException(e);
        }
    }
}