package io.riggo.data.repositories;

import io.riggo.data.domain.LoadLineItem;
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
public class LoadLineItemRepositoryTest {

    @Autowired
    private LoadLineItemRepository loadLineItemRepository;

    @Test
    public void findByLoadId() {
        //given
        Integer loadId = 1;

        //when
        Optional<List<LoadLineItem>> loadLineItemList = loadLineItemRepository.findByLoadId(loadId);

        //then loadLineItemsList is present
        assertTrue(loadLineItemList.isPresent());

        // and contains 2 load_line_items
        assertTrue(loadLineItemList.get().size() == 2);

    }
}
