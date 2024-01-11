package ru.javaops.restaurant_voting.repository;

import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurant_voting.model.Menu;

@Transactional(readOnly = true)
public interface MenuRepository extends BaseRepository<Menu>{
}
