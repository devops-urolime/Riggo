package io.riggo.data.repositories;

import io.riggo.data.domain.LoadStopSummary;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LoadStopSummaryRepositoryTest {

    @Autowired
    private LoadStopSummaryRepository loadStopSummaryRepository;

    @Test
    public void findStopSummaryBySiteId() {
        //given
        Integer siteId = 100;

        //when
        Optional<List<LoadStopSummary>> loadStopSummary = loadStopSummaryRepository.findStopSummaryBySiteId(siteId);

        //then loadStopSummary isPresent
        assertTrue(loadStopSummary.isPresent());
    }

    @Test
    public void findStopSummaryBySiteIdShipperId() {
        //given
        Integer siteId = 100;
        Integer shipperId = 1;

        //when
        Optional<List<LoadStopSummary>> loadStopSummary = loadStopSummaryRepository.findStopSummaryBySiteIdShipperId(siteId, shipperId);

        //then loadStopSummary isPresent
        assertTrue( loadStopSummary.isPresent());
    }
}