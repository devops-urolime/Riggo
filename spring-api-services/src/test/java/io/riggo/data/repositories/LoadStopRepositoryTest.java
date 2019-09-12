package io.riggo.data.repositories;

import io.riggo.data.domain.LoadLineItem;
import io.riggo.data.domain.LoadStop;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LoadStopRepositoryTest {

    @Autowired
    private LoadStopRepository loadStopRepository;

    @Test
    public void findLoadStopByExtSysIdSiteId() {
        //given
        Integer siteId = 100;

        //when
        Optional<LoadStop> loadStop = loadStopRepository.findByExtSysId("extSysId1", siteId);

        //then loadStopSummary isPresent
        Assert.assertTrue(loadStop.isPresent());
    }
}
