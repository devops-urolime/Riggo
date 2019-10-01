package io.riggo.data.repositories;

import io.riggo.data.domain.LoadPipeline;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoadPipelineRepository extends CrudRepository<LoadPipeline, Integer> {

    @Query(nativeQuery = true, value = "SELECT load_status, count(*) as count FROM load l " +
            " JOIN shipper s on s.id = l.shipper_id " +
            " WHERE l.site_id = :siteId AND s.site_id = :siteId GROUP BY l.load_status")
    Optional<List<LoadPipeline>> findPipelineSummaryBySiteId(@Param("siteId") Integer siteId);

    @Query(nativeQuery = true, value = "SELECT load_status, count(*) as count FROM load l JOIN shipper s ON s.id = l.shipper_id WHERE l.site_id = :siteId AND s.id = :shipperId AND s.site_id = :siteId GROUP BY l.load_status")
    Optional<List<LoadPipeline>> findPipelineSummaryBySiteIdShipperId(@Param("siteId") Integer siteId, @Param("shipperId") Integer shipperId);
}