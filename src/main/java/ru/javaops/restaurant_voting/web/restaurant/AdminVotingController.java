package ru.javaops.restaurant_voting.web.restaurant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javaops.restaurant_voting.model.Vote;
import ru.javaops.restaurant_voting.repository.VoteRepository;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = AdminVotingController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminVotingController {
    static final String REST_URL = "/api/admin/voting";

    private final VoteRepository voteRepository;

    public AdminVotingController(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @GetMapping
    public List<Vote> getAll() {
        log.info("get all votes");
        return voteRepository.findAll();
    }

    @GetMapping("/by-date")
    public List<Vote> getAllByDate(@RequestParam LocalDate date) {
        log.info("get all votes on date {}", date);
        return voteRepository.findAllByVoteDate(date);
    }

    @GetMapping("/by-user/{id}")
    public List<Vote> getAllByUser(@PathVariable int id) {
        log.info("get all votes of user with id {}", id);
        return voteRepository.findAllByUserId(id);
    }

    @GetMapping("/by-user-on-date/{id}")
    public List<Vote> getAllByUserOnDate(@PathVariable int id, @RequestParam LocalDate date) {
        log.info("get all votes of user with id {} on date {}", id, date);
        return voteRepository.findAllByUserIdAndVoteDate(id, date);
    }
}
