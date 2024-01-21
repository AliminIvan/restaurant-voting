package com.github.AliminIvan.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.github.AliminIvan.restaurantvoting.validation.NoHtml;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Table(name = "restaurant", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "address"}, name = "restaurant_unique_name_address_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Restaurant extends NamedEntity {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
//    @JsonManagedReference
    @ToString.Exclude
    private List<Menu> menus;

    @Column(name = "address", nullable = false)
    @NotBlank
    @NoHtml
    private String address;

    public Restaurant(Integer id, String name, List<Menu> menus, String address) {
        super(id, name);
        this.menus = menus;
        this.address = address;
    }
}
