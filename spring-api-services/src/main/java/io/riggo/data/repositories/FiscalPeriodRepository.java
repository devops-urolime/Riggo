package io.riggo.data.repositories;

import io.riggo.data.domain.FiscalPeriod;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface FiscalPeriodRepository extends CrudRepository<FiscalPeriod, Integer> {

    @Query("SELECT fp FROM FiscalPeriod fp WHERE dateActual = :dateActual")
    Optional<FiscalPeriod> findByDateActual(@Param("dateActual")LocalDate dateActual);
}