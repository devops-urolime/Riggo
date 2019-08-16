package io.riggo.data.repositories;

import io.riggo.data.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByEmailAndSiteId() {
        //given
        String email = "email@riggo.io";
        Integer siteId = 100;

        //when
        Optional<User> user = userRepository.findByEmailAndSiteId(email, siteId);

        //then menusBySiteAndType isPresent
        assertTrue( user.isPresent());
        assertTrue( user.get().getId() == 1 );
    }
}
