package io.riggo.data.repositories;

import io.riggo.data.domain.LoadPipeline;
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
public class LoadPipelineRepositoryTest {

    @Autowired
    private LoadPipelineRepository loadPipelineRepository;


    @Test
    public void findPipelineSummaryBySiteId() {
        //given
        Integer siteId = 100;

        //when
        Optional<List<LoadPipeline>> loadPipeline = loadPipelineRepository.findPipelineSummaryBySiteId(siteId);

        //then menusBySiteAndType isPresent
        assertTrue( loadPipeline.isPresent());
    }


    @Test
    public void findPipelineSummaryBySiteIdShipperId() {
        //given
        Integer siteId = 100;
        Integer shipperId = 1;

        //when
        Optional<List<LoadPipeline>> loadPipeline = loadPipelineRepository.findPipelineSummaryBySiteIdShipperId(siteId, shipperId);

        //then menusBySiteAndType isPresent
        assertTrue( loadPipeline.isPresent());
    }
}
