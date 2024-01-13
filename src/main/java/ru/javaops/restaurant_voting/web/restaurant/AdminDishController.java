package ru.javaops.restaurant_voting.web.restaurant;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.restaurant_voting.model.Dish;
import ru.javaops.restaurant_voting.model.Menu;
import ru.javaops.restaurant_voting.repository.DishRepository;
import ru.javaops.restaurant_voting.repository.MenuRepository;

import java.net.URI;
import java.util.List;

import static ru.javaops.restaurant_voting.web.RestValidation.assureIdConsistent;
import static ru.javaops.restaurant_voting.web.RestValidation.checkNew;

@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminDishController {
    static final String REST_URL = "/api/admin/dishes";

    private final DishRepository repository;

    private final MenuRepository menuRepository;

    public AdminDishController(DishRepository repository, MenuRepository menuRepository) {
        this.repository = repository;
        this.menuRepository = menuRepository;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete dish with id {}", id);
        repository.deleteExisted(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@Valid @RequestBody Dish dish, @RequestParam int menuId) {
        log.info("create dish {}", dish);
        checkNew(dish);
        Menu menu = menuRepository.getExisted(menuId);
        dish.setMenu(menu);
        Dish created = repository.save(dish);
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
        Dish dishFromDb = repository.getExisted(id);
        dish.setMenu(dishFromDb.getMenu());
        repository.save(dish);
    }

    @GetMapping
    public List<Dish> getAll() {
        log.info("get all dishes");
        return repository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }
}
