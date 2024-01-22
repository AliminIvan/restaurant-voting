package com.github.AliminIvan.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "vote", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "vote_date"}, name = "vote_unique_user_vote_date_idx")})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Vote extends BaseEntity {

    @Column(name = "vote_date", nullable = false, updatable = false)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate voteDate;

    @Column(name = "vote_time", nullable = false)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime voteTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(message = "voting must not be null")
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    public Vote(Integer id, User user, Restaurant restaurant, LocalDate voteDate, LocalTime voteTime) {
        super(id);
        this.user = user;
        this.restaurant = restaurant;
        this.voteDate = voteDate;
        this.voteTime = voteTime;
    }
}
