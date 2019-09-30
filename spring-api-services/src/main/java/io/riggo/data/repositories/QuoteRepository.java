package io.riggo.data.repositories;

import io.riggo.data.domain.Quote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuoteRepository extends CrudRepository<Quote, Integer> {

    @Query("select q from Quote q where q.extSysId = :extSysId")
    Optional<Quote> findByExtSysId(@Param("extSysId") String extSysId);
}