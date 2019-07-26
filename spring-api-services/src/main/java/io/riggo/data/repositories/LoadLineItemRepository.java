package io.riggo.data.repositories;

import io.riggo.data.domain.LoadLineItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoadLineItemRepository extends CrudRepository<LoadLineItem, Long> {

    @Query("select l from LoadLineItem l where l.loadId = :loadId")
    Optional<List<LoadLineItem>> findByLoadId(@Param("loadId") Integer loadId);

    @Query("select l from LoadLineItem l where l.extSysId = :extSysId")
    Optional<LoadLineItem> findByExtSysId(@Param("extSysId") String extSysId);
}
