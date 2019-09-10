package io.riggo.data.repositories;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LoadLineItemRepositoryTest {

    @Autowired
    private LoadLineItemRepository loadLineItemRepository;

    @Test
    public void aTest() {
        assertTrue(true);
    }
}
