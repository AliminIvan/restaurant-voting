package com.github.AliminIvan.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "menu", uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "lunch_date"}, name = "menu_unique_restaurant_id_lunch_date_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Menu extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @JsonBackReference
    private Restaurant restaurant;

    @Column(name = "lunch_date", nullable = false)
    @NotNull(message = "lunchDate must not be null")
    private LocalDate lunchDate = LocalDate.now();

    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @BatchSize(size = 20)
    private List<Dish> dishes;

    public Menu(Integer id, Restaurant restaurant, List<Dish> dishes) {
        super(id);
        this.restaurant = restaurant;
        this.dishes = dishes;
    }
}
