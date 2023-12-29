package ru.javaops.restaurant_voting.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "menu")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends BaseEntity {

    @OneToOne(mappedBy = "menu")
    private Restaurant restaurant;

    @Column(name = "lunch_date_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime lunchDateTime;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.REMOVE)
    private List<Dish> dishes;

    public Menu(Integer id, Restaurant restaurant, LocalDateTime lunchDateTime, List<Dish> dishes) {
        super(id);
        this.restaurant = restaurant;
        this.lunchDateTime = lunchDateTime;
        this.dishes = dishes;
    }
}
