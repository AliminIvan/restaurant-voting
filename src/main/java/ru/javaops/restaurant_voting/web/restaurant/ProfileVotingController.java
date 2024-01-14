package ru.javaops.restaurant_voting.web.restaurant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.javaops.restaurant_voting.error.DataConflictException;
import ru.javaops.restaurant_voting.model.Vote;
import ru.javaops.restaurant_voting.repository.RestaurantRepository;
import ru.javaops.restaurant_voting.repository.UserRepository;
import ru.javaops.restaurant_voting.repository.VoteRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static ru.javaops.restaurant_voting.util.DateTimeUtil.DEAD_LINE_TIME;
import static ru.javaops.restaurant_voting.web.AuthUser.authUser;

@RestController
@RequestMapping(value = ProfileVotingController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Transactional(readOnly = true)
public class ProfileVotingController {
    static final String REST_URL = "/api/voting";

    private final VoteRepository repository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    public ProfileVotingController(VoteRepository repository, UserRepository userRepository, RestaurantRepository restaurantRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping
    public List<Vote> getAll() {
        log.info("get all votes for user {}", authUser());
        return repository.getAll(getCurrentUserId());
    }

    @GetMapping("/{id}")
    public Optional<Vote> get(@PathVariable int id) {
        log.info("get vote with id: {}", id);
        return repository.get(getCurrentUserId(), id);
    }

    @GetMapping("/by-date")
    public Optional<Vote> getByDate(@RequestParam LocalDate date) {
        log.info("get vote by date for user {}", authUser());
        return repository.getByDate(getCurrentUserId(), date);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void createOrUpdate(@RequestParam int restaurantId) {
        log.info("user with id {} voting for restaurant with id {}", getCurrentUserId(), restaurantId);
        LocalDateTime currentDateTime = LocalDateTime.now();
        Optional<Vote> voteFromDb = repository.getByDate(getCurrentUserId(), currentDateTime.toLocalDate());
        if (currentDateTime.toLocalTime().isBefore(DEAD_LINE_TIME)) {
            Vote newVote;
            if (voteFromDb.isEmpty()) {
                newVote = new Vote(null,
                        userRepository.getExisted(getCurrentUserId()),
                        restaurantRepository.getExisted(restaurantId),
                        currentDateTime.toLocalDate(),
                        currentDateTime.toLocalTime());
            } else {
                newVote = voteFromDb.get();
                newVote.setRestaurant(restaurantRepository.getExisted(restaurantId));
                newVote.setVoteDate(currentDateTime.toLocalDate());
                newVote.setVoteTime(currentDateTime.toLocalTime());
            }
            repository.save(newVote);
        } else {
            throw new DataConflictException(String.format("User can`t vote or change vote after %s AM", DEAD_LINE_TIME));
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete vote with id {}", id);
        repository.delete(authUser().getId(), id);
    }

    private static Integer getCurrentUserId() {
        return authUser().getId();
    }
}
