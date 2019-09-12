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

    @Query("SELECT lli from LoadLineItem lli " +
            "JOIN Load l ON l.id = lli.loadId " +
            "WHERE lli.extSysId = :extSysId AND l.siteId = :siteId")
    Optional<LoadLineItem> findByExtSysId(@Param("extSysId") String extSysId, @Param("siteId") Integer siteId);
}
