package com.github.AliminIvan.restaurantvoting.repository;

import com.github.AliminIvan.restaurantvoting.model.Restaurant;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant>{

    Optional<Restaurant> getByNameAndAddress(String name, String address);
}
