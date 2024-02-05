package com.github.AliminIvan.restaurantvoting.repository;

import com.github.AliminIvan.restaurantvoting.model.Dish;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish>{

    @Transactional
    @Modifying
    int deleteDishByIdAndMenuId(int id, int menuId);

    Optional<Dish> getDishByIdAndMenuId(int id, int menuId);

    List<Dish> getAllByMenuId(int menuId);
}
