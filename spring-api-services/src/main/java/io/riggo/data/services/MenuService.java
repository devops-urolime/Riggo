package io.riggo.data.services;

import io.riggo.data.domain.Menu;
import io.riggo.data.exception.RiggoDataAccessException;
import io.riggo.data.repositories.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    public Optional<List<Menu>> findBySiteAndType(Long siteId, Integer menuType){
        try{
            return menuRepository.findBySiteAndType(siteId, menuType);
        }catch (Exception e){
            throw new RiggoDataAccessException(e);
        }
    }
}