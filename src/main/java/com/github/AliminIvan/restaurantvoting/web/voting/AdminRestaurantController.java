package com.github.AliminIvan.restaurantvoting.web.voting;

import com.github.AliminIvan.restaurantvoting.model.Menu;
import com.github.AliminIvan.restaurantvoting.model.Restaurant;
import com.github.AliminIvan.restaurantvoting.repository.MenuRepository;
import com.github.AliminIvan.restaurantvoting.repository.RestaurantRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.github.AliminIvan.restaurantvoting.web.RestValidation.assureIdConsistent;
import static com.github.AliminIvan.restaurantvoting.web.RestValidation.checkNew;

@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminRestaurantController {

    static final String REST_URL = "/api/admin/restaurants";

    private final RestaurantRepository restaurantRepository;

    private final MenuRepository menuRepository;

    public AdminRestaurantController(RestaurantRepository restaurantRepository, MenuRepository menuRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuRepository = menuRepository;
    }

    @GetMapping("/{id}")
    public Restaurant get(@PathVariable int id) {
        log.info("get restaurant with id: {}", id);
        return restaurantRepository.getExisted(id);
    }

    @GetMapping("/{id}/with-menu")
    public Restaurant getWithMenus(@PathVariable int id) {
        log.info("get restaurant with id : {} with it`s all menu`s", id);
        List<Menu> menus = menuRepository.getByRestaurantId(id);
        Restaurant restaurant = restaurantRepository.getExisted(id);
        restaurant.setMenus(menus);
        return restaurant;
    }

    @GetMapping
    public List<Restaurant> getAll() {
        log.info("get all restaurants");
        return restaurantRepository.findAll(Sort.by(Sort.Direction.ASC, "name", "address"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete restaurant with id {}", id);
        restaurantRepository.deleteExisted(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createWihLocation(@Valid @RequestBody Restaurant restaurant) {
        log.info("create restaurant {}", restaurant);
        checkNew(restaurant);
        Restaurant created = restaurantRepository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("update restaurant {} with id={}", restaurant, id);
        assureIdConsistent(restaurant, id);
        restaurantRepository.save(restaurant);
    }

}
