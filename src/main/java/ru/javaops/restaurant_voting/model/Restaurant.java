package ru.javaops.restaurant_voting.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends NamedEntity {

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "menu_id", referencedColumnName = "id")
    private Menu menu;

    @Column(name = "address")
    private String address;

    public Restaurant(Integer id, String name, Menu menu, String address) {
        super(id, name);
        this.menu = menu;
        this.address = address;
    }
}
