package io.riggo.data.repositories;

import io.riggo.data.domain.FiscalPeriod;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class FiscalPeriodRepositoryTest {

    @Autowired
    private FiscalPeriodRepository fiscalPeriodRepository;


    @Test
    public void findByDateActual() {
        //given
        LocalDate dateActual = LocalDate.of(2019, 8, 21);
        //when
        Optional<FiscalPeriod> fiscalPeriod = fiscalPeriodRepository.findByDateActual(dateActual);
        //then
        assertTrue(fiscalPeriod.isPresent());
    }
}