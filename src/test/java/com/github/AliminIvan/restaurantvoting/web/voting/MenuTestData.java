package com.github.AliminIvan.restaurantvoting.web.voting;

import com.github.AliminIvan.restaurantvoting.model.Menu;
import com.github.AliminIvan.restaurantvoting.web.MatcherFactory;

import java.time.LocalDate;
import java.util.List;

public class MenuTestData {

    public static final MatcherFactory.Matcher<Menu> MENU_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Menu.class, "restaurant", "dishes");
    public static final int MENU1_ID = 1;
    public static final int MENU2_ID = 2;
    public static final int MENU3_ID = 3;
    public static final int MENU4_ID = 4;
    public static final int MENU5_ID = 5;
    public static final int MENU6_ID = 6;
    public static final Menu menu1 = new Menu(MENU1_ID, null, null);
    public static final LocalDate NEW_YEAR = LocalDate.of(2023, 12, 31);
    public static final List<Menu> menusWithDishesForRestaurant1 = List.of(
            new Menu(MENU1_ID, null, List.of(DishTestData.dish1, DishTestData.dish2, DishTestData.dish3)));
    public static final List<Menu> menusWithDishesForRestaurant2 = List.of(
            new Menu(MENU6_ID, null, List.of()));

    public static Menu getUpdated() {
        Menu updatedMenu = new Menu(MENU1_ID, RestaurantTestData.restaurant1, null);
        updatedMenu.setLunchDate(LocalDate.now().minusDays(1));
        return updatedMenu;
    }
}
