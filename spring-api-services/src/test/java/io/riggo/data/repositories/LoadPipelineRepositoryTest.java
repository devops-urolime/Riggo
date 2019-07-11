package io.riggo.data.repositories;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import io.riggo.data.domain.LoadPipeline;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LoadPipelineRepositoryTest {

    @Autowired
    private LoadPipelineRepository loadPipelineRepository;


    @Test
    public void findPipelineSummaryBySiteIdShipperId() {
        //given
        Long siteId = 100l;
        Long shipperId = 100l;

        //when
        Optional<LoadPipeline> loadPipeline = loadPipelineRepository.findPipelineSummaryBySiteIdShipperId(siteId, shipperId);

        //then menusBySiteAndType isPresent
        assertTrue( loadPipeline.isPresent());
    }
}
