package io.riggo.data.repositories;

import io.riggo.data.domain.LoadLineItem;
import io.riggo.data.domain.LoadStopSummary;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LoadLineItemRepositoryTest {

    @Autowired
    private LoadLineItemRepository loadLineItemRepository;

    @Test
    public void findLoadLineItemByExtSysIdSiteId() {
        //given
        Integer siteId = 100;

        //when
        Optional<LoadLineItem> loadLineItem = loadLineItemRepository.findByExtSysId("extSysId1", siteId);

        //then loadStopSummary isPresent
        Assert.assertTrue(loadLineItem.isPresent());
    }
}
