package com.github.AliminIvan.restaurantvoting.repository;

import com.github.AliminIvan.restaurantvoting.model.Restaurant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant>{
    @EntityGraph(attributePaths = {"menus"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT r FROM Restaurant r WHERE r.id=?1")
    Optional<Restaurant> getWithMenus(int id);

    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.menus m " +
            "WHERE m.lunchDate=?1 " +
            "ORDER BY r.name, r.address")
    List<Restaurant> getWithMenusByDate(LocalDate ld);

    Optional<Restaurant> getByNameAndAddress(String name, String address);
}
