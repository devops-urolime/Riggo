package io.riggo.data.repositories;

import io.riggo.data.domain.LoadPipeline;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoadPipelineRepository extends CrudRepository<LoadPipeline, Long> {

    @Query(nativeQuery = true, value = "select load_status, count(*) as count from load group by load_status")
    Optional<List<LoadPipeline>> findPipelineSummaryBySiteIdShipperId(Long siteId, Long shipperId);
}