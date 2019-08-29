package io.riggo.data.repositories;

import io.riggo.data.domain.LoadStopSummary;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoadStopSummaryRepository extends CrudRepository<LoadStopSummary, String> {

    @Query(nativeQuery = true, value = "SELECT " +
            "CONCAT(ls.type,'-',ls.arrival_status) AS id, ls.type, ls.arrival_status, count(*) AS count " +
            "FROM load l " +
            "JOIN shipper s ON s.id = l.shipper_id " +
            "JOIN load_stop ls ON ls.load_id = l.id " +
            "WHERE s.site_id = :siteId " +
            "GROUP BY ls.type, ls.arrival_status")
    Optional<List<LoadStopSummary>> findStopSummaryBySiteId(@Param("siteId") Integer siteId);

    @Query(nativeQuery = true, value = "SELECT CONCAT(ls.type,'-',ls.arrival_status) AS id, ls.type, ls.arrival_status, count(*) AS count " +
            "FROM load l " +
            "JOIN shipper s ON s.id = l.shipper_id " +
            "JOIN load_stop ls ON ls.load_id = l.id " +
            "WHERE s.site_id = :siteId AND s.id = :shipperId " +
            "GROUP BY ls.type, ls.arrival_status")
    Optional<List<LoadStopSummary>> findStopSummaryBySiteIdShipperId(@Param("siteId") Integer siteId, @Param("shipperId") Integer shipperId);
}
