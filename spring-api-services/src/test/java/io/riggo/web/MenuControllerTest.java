package io.riggo.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import io.riggo.data.domain.Menu;
import io.riggo.data.services.MenuService;
import static java.util.Collections.singletonList;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.given;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MenuController.class)
public class MenuControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MenuService menuService;

    private static Logger logger = LoggerFactory.getLogger(MenuControllerTest.class);

    @WithMockUser(value = "spring1")
    @Test
    public void getMenusBySiteAndType() throws Exception {
        Menu dashboardMenu = new Menu();
        dashboardMenu.setName("Dashboard");

        List<Menu> allMenus = singletonList(dashboardMenu);

        given(menuService.findBySiteAndType(100l, 1)).willReturn(java.util.Optional.of(allMenus));

        MvcResult result = mvc.perform(get(Paths.API + Paths.VERSION + Paths.MENUS)
                .param("type", "1")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].name", is(dashboardMenu.getName())))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        logger.error(content);
    }

    @WithMockUser(value = "spring1")
    @Test
    public void getMenusBySiteAndTypeReturns400OnBadTypeParam() throws Exception {
        mvc.perform(get(Paths.API + Paths.VERSION + Paths.MENUS + "?type=123")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mvc.perform(get(Paths.API + Paths.VERSION + Paths.MENUS + "?type=a")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
