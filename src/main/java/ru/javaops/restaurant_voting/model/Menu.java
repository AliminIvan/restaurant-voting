package ru.javaops.restaurant_voting.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.javaops.restaurant_voting.util.DateTimeUtil;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "menu", uniqueConstraints = {@UniqueConstraint(columnNames = {"RESTAURANT_ID", "LUNCH_DATE"}, name = "menu_unique_restaurant_id_lunch_date_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(name = "lunch_date", nullable = false)
    @DateTimeFormat(pattern = DateTimeUtil.DATE_PATTERN)
    private LocalDate lunchDate = LocalDate.now();

    @OneToMany(mappedBy = "menu", cascade = CascadeType.REMOVE)
    private List<Dish> dishes;

    public Menu(Integer id, Restaurant restaurant, List<Dish> dishes) {
        super(id);
        this.restaurant = restaurant;
        this.dishes = dishes;
    }
}
