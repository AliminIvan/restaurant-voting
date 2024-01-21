package com.github.AliminIvan.restaurantvoting.web.voting;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.github.AliminIvan.restaurantvoting.model.Dish;
import com.github.AliminIvan.restaurantvoting.model.Menu;
import com.github.AliminIvan.restaurantvoting.repository.DishRepository;
import com.github.AliminIvan.restaurantvoting.repository.MenuRepository;

import java.net.URI;
import java.util.List;

import static com.github.AliminIvan.restaurantvoting.web.RestValidation.assureIdConsistent;
import static com.github.AliminIvan.restaurantvoting.web.RestValidation.checkNew;

@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminDishController {
    static final String REST_URL = "/api/admin/dishes";

    private final DishRepository dishRepository;

    private final MenuRepository menuRepository;

    public AdminDishController(DishRepository dishRepository, MenuRepository menuRepository) {
        this.dishRepository = dishRepository;
        this.menuRepository = menuRepository;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete dish with id {}", id);
        dishRepository.deleteExisted(id);
    }

    @PostMapping(value = "/{menuId}",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@Valid @RequestBody Dish dish, @PathVariable int menuId) {
        log.info("create dish {}", dish);
        checkNew(dish);
        Menu menu = menuRepository.getExisted(menuId);
        dish.setMenu(menu);
        Dish created = dishRepository.save(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Dish dish, @PathVariable int id) {
        log.info("update dish {} with id={}", dish, id);
        assureIdConsistent(dish, id);
        Dish dishFromDb = dishRepository.getExisted(id);
        dish.setMenu(dishFromDb.getMenu());
        dishRepository.save(dish);
    }

    @GetMapping("/{id}")
    public Dish get(@PathVariable int id) {
        log.info("get dish with id: {}", id);
        return dishRepository.getExisted(id);
    }

    @GetMapping
    public List<Dish> getAllByMenu(@RequestParam int menuId) {
        log.info("get all dishes in menu with id: {}", menuId);
        return dishRepository.getAllByMenuId(menuId);
    }
}
