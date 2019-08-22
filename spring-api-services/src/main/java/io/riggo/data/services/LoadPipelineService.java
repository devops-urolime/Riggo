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

    public Optional<List<LoadPipeline>> findPipelineSummaryBySiteId(Integer siteId) {
        try{
            return loadPipelineRepository.findPipelineSummaryBySiteId(siteId);
        }catch (Exception e){
            throw new RiggoDataAccessException(e);
        }
    }

    public Optional<List<LoadPipeline>> findPipelineSummaryBySiteIdShipperId(Integer siteId, Integer shipperId) {
        try{
            return loadPipelineRepository.findPipelineSummaryBySiteIdShipperId(siteId, shipperId);
        }catch (Exception e){
            throw new RiggoDataAccessException(e);
        }
    }
}