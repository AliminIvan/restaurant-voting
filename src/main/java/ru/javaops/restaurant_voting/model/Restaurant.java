package ru.javaops.restaurant_voting.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Table(name = "restaurant", uniqueConstraints = {@UniqueConstraint(columnNames = {"ADDRESS", "NAME"}, name = "restaurant_unique_address_name_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends NamedEntity {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OnDelete(action = OnDeleteAction.CASCADE)
//    @JsonManagedReference
    private List<Menu> menus;

    @Column(name = "address", nullable = false)
    private String address;

    public Restaurant(Integer id, String name, List<Menu> menus, String address) {
        super(id, name);
        this.menus = menus;
        this.address = address;
    }
}
