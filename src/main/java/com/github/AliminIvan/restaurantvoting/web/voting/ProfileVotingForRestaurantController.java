package com.github.AliminIvan.restaurantvoting.web.voting;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.AliminIvan.restaurantvoting.View;
import com.github.AliminIvan.restaurantvoting.error.DataConflictException;
import com.github.AliminIvan.restaurantvoting.model.Menu;
import com.github.AliminIvan.restaurantvoting.model.Restaurant;
import com.github.AliminIvan.restaurantvoting.model.Vote;
import com.github.AliminIvan.restaurantvoting.repository.MenuRepository;
import com.github.AliminIvan.restaurantvoting.repository.RestaurantRepository;
import com.github.AliminIvan.restaurantvoting.repository.VoteRepository;
import com.github.AliminIvan.restaurantvoting.web.AuthUser;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.github.AliminIvan.restaurantvoting.util.DateTimeUtil.DEAD_LINE_TIME;
import static com.github.AliminIvan.restaurantvoting.web.RestValidation.checkNew;

@RestController
@RequestMapping(value = ProfileVotingForRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Transactional(readOnly = true)
public class ProfileVotingForRestaurantController {
    static final String REST_URL = "/api/votes";

    private final VoteRepository voteRepository;

    private final RestaurantRepository restaurantRepository;

    private final MenuRepository menuRepository;

    public ProfileVotingForRestaurantController(VoteRepository voteRepository, RestaurantRepository restaurantRepository, MenuRepository menuRepository) {
        this.voteRepository = voteRepository;
        this.restaurantRepository = restaurantRepository;
        this.menuRepository = menuRepository;
    }

    @GetMapping
    @JsonView(View.Public.class)
    public List<Vote> getAll(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get all votes for user {}", authUser);
        return voteRepository.getAll(authUser.id());
    }

    @GetMapping("/{id}")
    @JsonView(View.Public.class)
    public ResponseEntity<Vote> get(@PathVariable int id, @AuthenticationPrincipal AuthUser authUser) {
        log.info("get vote with id: {}", id);
        return ResponseEntity.of(voteRepository.get(authUser.id(), id));
    }

    @GetMapping("/by-date")
    @JsonView(View.Public.class)
    public ResponseEntity<Vote> getByDate(@RequestParam LocalDate date, @AuthenticationPrincipal AuthUser authUser) {
        log.info("get vote by date for user {}", authUser);
        return ResponseEntity.of(voteRepository.getByDate(authUser.id(), date));
    }

    @GetMapping("/{id}/with-menu")
    public Restaurant getRestaurantWithMenus(@PathVariable int id) {
        log.info("get restaurant with id : {} with it`s all menu`s", id);
        Optional<Menu> menu = menuRepository.getByRestaurantAndLunchDate(id, LocalDate.now());
        Restaurant restaurant = restaurantRepository.getExisted(id);
        restaurant.setMenus(List.of(menu.orElseThrow()));
        return restaurant;
    }

    @GetMapping("/restaurants-with-menus")
    @Cacheable("restaurants")
    public List<Restaurant> getRestaurantsWithMenus() {
        LocalDate currentDate = LocalDate.now();
        log.info("get restaurants menu on date {}", currentDate);
        List<Restaurant> allRestaurants = restaurantRepository.findAll();
        Map<Integer, Menu> menuMap = new HashMap<>();
        menuRepository.getAllByLunchDate(currentDate).forEach(menu -> menuMap.put(menu.getRestaurant().getId(), menu));
        allRestaurants.forEach(restaurant -> restaurant.setMenus(List.of(menuMap.get(restaurant.getId()))));
        return allRestaurants;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<Vote> createWihLocation(@Valid @RequestBody Vote vote, @AuthenticationPrincipal AuthUser authUser) {
        log.info("create vote {}", vote);
        checkNew(vote);
        prepareToSave(vote, LocalDateTime.now(), authUser);
        Vote created = voteRepository.save(vote);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@Valid @RequestBody Vote vote, @AuthenticationPrincipal AuthUser authUser) {
        log.info("update vote");
        LocalDateTime currentDateTime = LocalDateTime.now();
        Integer voteId = voteRepository.getByDate(authUser.id(), currentDateTime.toLocalDate()).orElseThrow().getId();
        vote.setId(voteId);
        if (currentDateTime.toLocalTime().isBefore(DEAD_LINE_TIME)) {
            prepareToSave(vote, currentDateTime, authUser);
            voteRepository.save(vote);
        } else {
            throw new DataConflictException(String.format("User can`t change vote after %s o`clock", DEAD_LINE_TIME));
        }
    }

    private void prepareToSave(Vote vote, LocalDateTime currentDateTime, AuthUser authUser) {
        vote.setVoteDate(currentDateTime.toLocalDate());
        vote.setVoteTime(currentDateTime.toLocalTime());
        String name = vote.getRestaurant().getName();
        String address = vote.getRestaurant().getAddress();
        vote.setRestaurant(restaurantRepository.getByNameAndAddress(name, address).orElseThrow());
        vote.setUser(authUser.getUser());
    }
}
