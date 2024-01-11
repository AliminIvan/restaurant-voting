package ru.javaops.restaurant_voting.repository;

import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurant_voting.model.Dish;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish>{
}
