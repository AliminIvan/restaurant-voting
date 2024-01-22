package com.github.AliminIvan.restaurantvoting.repository;

import com.github.AliminIvan.restaurantvoting.model.Vote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    @Query("SELECT v FROM Vote v LEFT JOIN FETCH v.restaurant WHERE v.user.id=?1 AND v.id=?2")
    Optional<Vote> get(int userId, int id);

    @Query("SELECT v FROM Vote v LEFT JOIN FETCH v.restaurant WHERE v.user.id=?1")
    List<Vote> getAll(int id);

    @Query("SELECT v FROM Vote v LEFT JOIN FETCH v.restaurant WHERE v.user.id=?1 AND v.voteDate=?2")
    Optional<Vote> getByDate(int id, LocalDate ld);
}
