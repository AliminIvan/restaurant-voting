package ru.javaops.restaurant_voting.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "vote", uniqueConstraints = {@UniqueConstraint(columnNames = {"USER_ID", "CREATED"}, name = "vote_unique_user_created_idx")})
@Getter
@Setter
@NoArgsConstructor//(access = AccessLevel.PROTECTED)
public class Vote extends BaseEntity {

    @Column(name = "created", nullable = false, columnDefinition = "timestamp default now()", updatable = false)
    @NotNull
    private Date created = new Date();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    public Vote(Integer id, User user, Restaurant restaurant) {
        super(id);
        this.user = user;
        this.restaurant = restaurant;
    }
}
