package ru.javaops.restaurant_voting.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.javaops.restaurant_voting.validation.NoHtml;

import java.util.List;

@Entity
@Table(name = "restaurant", uniqueConstraints = {@UniqueConstraint(columnNames = {"ADDRESS", "NAME"}, name = "restaurant_unique_address_name_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends NamedEntity {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
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
