package ru.javaops.restaurant_voting.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurant_voting.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    @Query("SELECT v FROM Vote v WHERE v.user.id=?1 AND v.id=?2")
    Optional<Vote> get(int userId, int id);

    @Query("SELECT v FROM Vote v WHERE v.user.id=?1")
    List<Vote> getAll(int id);

    @Query("SELECT v FROM Vote v WHERE v.user.id=?1 AND v.voteDate=?2")
    Optional<Vote> getByDate(int id, LocalDate ld);

    List<Vote> findAllByVoteDate(LocalDate date);

    List<Vote> findAllByUserId(int id);

    List<Vote> findAllByUserIdAndVoteDate(int id, LocalDate date);

    @Transactional
    @Modifying
    @Query("DELETE FROM Vote v WHERE v.user.id=?1 AND v.id=?2")
    int delete(int userid, int id);
}
