package io.riggo.data.repositories;

import io.riggo.data.domain.Invoice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceRepository extends CrudRepository<Invoice, Integer> {

    @Query("select i from Invoice i where i.extSysId = :extSysId")
    Optional<Invoice> findByExtSysId(@Param("extSysId") String extSysId);
}