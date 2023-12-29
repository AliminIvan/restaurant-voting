package ru.javaops.restaurant_voting.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "restaurant", uniqueConstraints = {@UniqueConstraint(columnNames = {"ADDRESS", "NAME"}, name = "restaurant_unique_address_name_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends NamedEntity {

    @OneToOne(cascade = CascadeType.REMOVE, mappedBy = "restaurant")
    private Menu menu;

    @Column(name = "address", nullable = false)
    private String address;

    public Restaurant(Integer id, String name, Menu menu, String address) {
        super(id, name);
        this.menu = menu;
        this.address = address;
    }
}
