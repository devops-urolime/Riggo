package io.riggo;

import io.riggo.web.LoadController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SwaggerStartApplicationTests {

    @Autowired
    private LoadController controller;

    @Test
	public void contextLoads() {
        assertThat(controller).isNotNull();
	}

}
