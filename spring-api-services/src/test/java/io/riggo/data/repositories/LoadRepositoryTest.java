package io.riggo.data.repositories;

import java.util.Optional;

import org.apache.commons.codec.binary.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import io.riggo.data.domain.Load;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LoadRepositoryTest {

    @Autowired
    private LoadRepository loadRepository;

    @Test
    public void findByExtSysId() {
        //given
        String extSysId = "extSysId1";

        //when
        Optional<Load> load = loadRepository.findByExtSysId(extSysId);

        //then menusBySiteAndType isPresent
        assertTrue( load.isPresent());
        assertTrue( StringUtils.equals(load.get().getExtSysId(), extSysId) );

    }
}
