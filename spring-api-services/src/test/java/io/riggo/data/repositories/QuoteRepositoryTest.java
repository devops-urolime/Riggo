package io.riggo.data.repositories;

import io.riggo.data.domain.Quote;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class QuoteRepositoryTest {

    @Autowired
    private QuoteRepository quoteRepository;

    @Test
    public void findByExtSysId() {
        //given

        //when
        Optional<Quote> quoteRepositoryByExtSysId = quoteRepository.findByExtSysId("extSysId1");

        //then invoice isPresent and id = 1
        assertTrue(quoteRepositoryByExtSysId.isPresent());
        assertTrue(quoteRepositoryByExtSysId.get().getId() == 1);
    }
}
