package io.riggo.data.repositories;

import io.riggo.data.domain.LoadPipeline;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoadPipelineRepository extends CrudRepository<LoadPipeline, Long> {

    @Query( nativeQuery = true, value = "select id, (select count(pending) from load pending where load_status IN (1,2) ) as pending, ( select count(*) as inTransit from load where load_status IN (3,4,5,6)) as inTransit, (select count(*) as delivered from load where load_status IN (7,8)) as delivered from load where id = 1")
    Optional<LoadPipeline> findPipelineSummaryBySiteIdShipperId(Long siteId, Long shipperId);
}