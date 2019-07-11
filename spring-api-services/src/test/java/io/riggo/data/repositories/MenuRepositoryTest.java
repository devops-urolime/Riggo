package io.riggo.data.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import io.riggo.data.domain.Menu;
import io.riggo.data.domain.MenuType;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MenuRepositoryTest {

    @Autowired
    private MenuRepository menuRepository;

    @Test
    public void findBySiteAndType() {
        //given
        Long siteId = 100l;
        Integer menuType = MenuType.LEFT_HAND_MENU.getColVal();

        //when
        Optional<List<Menu>> menusBySiteAndType = menuRepository.findBySiteAndType(siteId, menuType);

        //then menusBySiteAndType isPresent
        assertTrue( menusBySiteAndType.isPresent());

        //then menusBySiteAndType's elements are ordered by rank
        menusBySiteAndType.get().stream()
                .reduce((menu1, menu2) -> {
                    assertTrue( menu1.getRank() <= menu2.getRank()); return menu1; } );

    }
}
