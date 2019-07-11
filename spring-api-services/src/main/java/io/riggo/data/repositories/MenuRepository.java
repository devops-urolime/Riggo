package io.riggo.data.repositories;

import io.riggo.data.domain.Menu;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends CrudRepository<Menu, Long> {

    @Query("select m from Menu m where m.siteId = :siteId and m.type = :menuType order by m.rank")
    Optional<List<Menu>> findBySiteAndType(@Param("siteId") Long siteId, @Param("menuType") Integer menuType);
}
