package ru.javaops.restaurant_voting.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.restaurant_voting.model.Menu;
import ru.javaops.restaurant_voting.model.Restaurant;
import ru.javaops.restaurant_voting.repository.MenuRepository;
import ru.javaops.restaurant_voting.repository.RestaurantRepository;

import java.net.URI;
import java.util.List;

import static ru.javaops.restaurant_voting.web.RestValidation.assureIdConsistent;
import static ru.javaops.restaurant_voting.web.RestValidation.checkNew;

@RestController
@RequestMapping(value = AdminMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminMenuController {

    static final String REST_URL = "/api/admin/menus";

    private final MenuRepository repository;

    private final RestaurantRepository restaurantRepository;

    public AdminMenuController(MenuRepository repository, RestaurantRepository restaurantRepository) {
        this.repository = repository;
        this.restaurantRepository = restaurantRepository;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete menu with id {}", id);
        repository.deleteExisted(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> createWithLocation(@RequestBody Menu menu, @RequestParam int restaurantId) {
        log.info("create menu {}", menu);
        checkNew(menu);
        Restaurant restaurant = restaurantRepository.getExisted(restaurantId);
        menu.setRestaurant(restaurant);
        Menu created = repository.save(menu);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Menu menu, @PathVariable int id) {
        log.info("update menu {} with id={}", menu, id);
        assureIdConsistent(menu, id);
        Menu menuFromDb = repository.getExisted(id);
        menu.setRestaurant(menuFromDb.getRestaurant());
        repository.save(menu);
    }

    @GetMapping
    public List<Menu> getAll() {
        log.info("get all menus");
        return repository.findAll(Sort.by(Sort.Direction.DESC, "lunchDate"));
    }


}
