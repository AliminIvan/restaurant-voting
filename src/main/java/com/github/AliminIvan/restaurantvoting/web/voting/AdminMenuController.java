package com.github.AliminIvan.restaurantvoting.web.voting;

import com.github.AliminIvan.restaurantvoting.model.Menu;
import com.github.AliminIvan.restaurantvoting.model.Restaurant;
import com.github.AliminIvan.restaurantvoting.repository.MenuRepository;
import com.github.AliminIvan.restaurantvoting.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static com.github.AliminIvan.restaurantvoting.web.RestValidation.assureIdConsistent;

@RestController
@RequestMapping(value = AdminMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminMenuController {

    static final String REST_URL = "/api/admin/menus";

    private final MenuRepository menuRepository;

    private final RestaurantRepository restaurantRepository;

    public AdminMenuController(MenuRepository menuRepository, RestaurantRepository restaurantRepository) {
        this.menuRepository = menuRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete menu with id {}", id);
        menuRepository.deleteExisted(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> createWithLocation(@RequestBody Integer restaurantId) {
        log.info("create menu for restaurant with id {}", restaurantId);
        Restaurant restaurant = restaurantRepository.getExisted(restaurantId);
        Menu menu = new Menu(null, null, List.of());
        menu.setRestaurant(restaurant);
        Menu created = menuRepository.save(menu);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody LocalDate lunchDate, @PathVariable int id) {
        log.info("update menu with id={}", id);
        Menu menuFromDb = menuRepository.getExisted(id);
        Menu menu = new Menu(null, null, null);
        menu.setLunchDate(lunchDate);
        menu.setRestaurant(menuFromDb.getRestaurant());
        assureIdConsistent(menu, id);
        menuRepository.save(menu);
    }

    @GetMapping("/{id}")
    public Menu get(@PathVariable int id) {
        log.info("get menu with id: {}", id);
        return menuRepository.getExisted(id);
    }

    @GetMapping("/by-restaurant")
    @Cacheable("menus")
    public List<Menu> getAllByRestaurant(@RequestParam int restaurantId) {
        log.info("get menus for restaurant with id: {}", restaurantId);
        return menuRepository.getByRestaurantId(restaurantId);
    }

    @GetMapping("/by-date")
    @Cacheable("menus")
    public List<Menu> getAllByDate(@RequestParam LocalDate date) {
        log.info("get menus on date: {}", date);
        return menuRepository.getAllByLunchDate(date);
    }

    @GetMapping("/by-restaurant-and-date")
    public Menu getByRestaurantAndDate(@RequestParam int restaurantId, @RequestParam LocalDate date) {
        log.info("get menu for restaurant with id {} on date {}", restaurantId, date);
        return menuRepository.getByRestaurantAndLunchDate(restaurantId, date).orElseThrow();
    }
}
