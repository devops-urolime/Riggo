package io.riggo.data.repositories;

import io.riggo.data.domain.LoadStopSummary;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoadStopSummaryRepository extends CrudRepository<LoadStopSummary, String> {

    @Query(nativeQuery = true, value = "select CONCAT(ls.type,'-',ls.arrival_status) as id, ls.type, ls.arrival_status, count(*) as count from load_stop ls where ls.arrival_status is not null group by ls.type, ls.arrival_status")
    Optional<List<LoadStopSummary>> findStopSummaryBySiteIdShipperId(Integer siteId, Integer shipperId);
}
