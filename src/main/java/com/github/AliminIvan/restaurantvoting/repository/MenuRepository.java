package com.github.AliminIvan.restaurantvoting.repository;

import com.github.AliminIvan.restaurantvoting.model.Menu;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface MenuRepository extends BaseRepository<Menu>{
    @Query("SELECT m FROM Menu m LEFT JOIN FETCH m.restaurant r WHERE m.lunchDate = ?1")
    List<Menu> getAllByLunchDate(LocalDate ld);

    List<Menu> getByRestaurantId(int id, Sort sort);
}
