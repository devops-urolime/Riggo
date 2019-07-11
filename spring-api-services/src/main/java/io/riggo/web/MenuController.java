package io.riggo.web;

import io.riggo.data.domain.Menu;
import io.riggo.data.domain.MenuType;
import io.riggo.data.services.MenuService;
import io.riggo.web.response.BaseAPIResponse;
import io.riggo.web.response.BaseAPIResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = Paths.API_VERSION)
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping(value = Paths.MENUS, produces = "application/json")
    @ResponseBody
    //@Cacheable(value = "menus", key = "#m0", unless = "#result == null")
    public BaseAPIResponse<Menu> getMenusBySiteAndType(@RequestParam("type") Integer type ) {
        //TODO: obtain siteId from JWT Token.  Make this Resource Context Aware.
        //TODO: Is this how we cache a list?  How do we expire the cache?
        MenuType menuType = MenuType.fromColVal(type);
        if(menuType != null) {
            return new BaseAPIResponseWrapper<Menu>().wrapForResponseListOfObjects(menuService.findBySiteAndType(100l, menuType.getColVal()));
        }else{
            throw new IllegalArgumentException("The 'type' parameter does not map to a menutype.");
        }
    }
}