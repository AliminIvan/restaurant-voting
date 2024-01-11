package ru.javaops.restaurant_voting.repository;

import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurant_voting.model.Vote;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote>{
}
