package io.riggo.data.repositories;

import io.riggo.data.domain.Shipper;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ShipperRepositoryTest {

    @Autowired
    private ShipperRepository shipperRepository;

    @Test
    public void findByExtSysId() {
        //given
        String extSysId = "extSysId1";
        Integer siteId = 100;

        //when
        Optional<Shipper> shipper = shipperRepository.findByExtSysId(extSysId, siteId);

        //then menusBySiteAndType isPresent
        assertTrue( shipper.isPresent());
        assertTrue( StringUtils.equals(shipper.get().getExtSysId(), extSysId) );
    }

    @Test
    public void findByEmailAndSiteId(){
        //given
        String email = "email@riggo.io";
        Integer siteId = 100;

        Optional<Shipper> shipper = shipperRepository.findByEmailAndSiteId(email, siteId);

        assertTrue( shipper.isPresent());
        assertTrue( shipper.get().getId() == 1);
    }

}