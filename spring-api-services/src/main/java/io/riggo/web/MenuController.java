package io.riggo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.riggo.data.domain.Menu;
import io.riggo.data.domain.MenuType;
import io.riggo.data.services.MenuService;
import io.riggo.web.response.BaseAPIResponse;
import io.riggo.web.response.BaseAPIResponseWrapper;

@RestController
@RequestMapping(value = Paths.API_VERSION)
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping(value = Paths.MENUS, produces = "application/json")
    @ResponseBody
    @PreAuthorize("hasAuthority('read:menu')")
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
