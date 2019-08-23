package io.riggo.data.services;

import io.riggo.data.domain.FiscalPeriod;
import io.riggo.data.exception.RiggoDataAccessException;
import io.riggo.data.repositories.FiscalPeriodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class FiscalPeriodService {

    @Autowired
    private FiscalPeriodRepository fiscalPeriodRepository;

    public Optional<FiscalPeriod> findByDateActual(LocalDate localDate) {
        try {
            return fiscalPeriodRepository.findByDateActual(localDate);
        } catch (Exception e) {
            throw new RiggoDataAccessException(e);
        }
    }
}
