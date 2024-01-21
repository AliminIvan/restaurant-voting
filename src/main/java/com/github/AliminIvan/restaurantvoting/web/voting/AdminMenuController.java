package com.github.AliminIvan.restaurantvoting.web.voting;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.github.AliminIvan.restaurantvoting.model.Menu;
import com.github.AliminIvan.restaurantvoting.model.Restaurant;
import com.github.AliminIvan.restaurantvoting.repository.MenuRepository;
import com.github.AliminIvan.restaurantvoting.repository.RestaurantRepository;

import java.net.URI;
import java.util.List;

import static com.github.AliminIvan.restaurantvoting.web.RestValidation.assureIdConsistent;
import static com.github.AliminIvan.restaurantvoting.web.RestValidation.checkNew;

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
    public ResponseEntity<Menu> createWithLocation(@Valid @RequestBody Menu menu) {
        log.info("create menu {}", menu);
        checkNew(menu);
        Restaurant restaurant = restaurantRepository.getByNameAndAddress(menu.getRestaurant().getName(),
                menu.getRestaurant().getAddress()).orElseThrow();
        menu.setRestaurant(restaurant);
        Menu created = menuRepository.save(menu);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Menu menu, @PathVariable int id) {
        log.info("update menu {} with id={}", menu, id);
        assureIdConsistent(menu, id);
        Menu menuFromDb = menuRepository.getExisted(id);
        menu.setRestaurant(menuFromDb.getRestaurant());
        menuRepository.save(menu);
    }

    @GetMapping("/{id}")
    public Menu get(@PathVariable int id) {
        log.info("get menu with id: {}", id);
        return menuRepository.getExisted(id);
    }

    @GetMapping("/by-restaurant")
    public List<Menu> getAllByRestaurant(@RequestParam int restaurantId) {
        log.info("get menu of voting with id: {}", restaurantId);
        return menuRepository.getByRestaurantId(restaurantId, Sort.by(Sort.Direction.DESC, "lunchDate"));
    }
}
