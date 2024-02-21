package com.github.AliminIvan.restaurantvoting.web.voting;

import com.github.AliminIvan.restaurantvoting.model.Restaurant;
import com.github.AliminIvan.restaurantvoting.web.MatcherFactory;

import java.util.List;

public class RestaurantTestData {

    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "menus");
    public static final int RESTAURANT1_ID = 1;
    public static final int RESTAURANT2_ID = 2;
    public static final int RESTAURANT3_ID = 3;
    public static final int RESTAURANT4_ID = 4;
    public static final int RESTAURANT5_ID = 5;
    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT1_ID, "Pasta Na Solyanke",
            List.of(), "Solyanka, 2/6, Bldg. 1, Moscow 109240 Russia");
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT2_ID, "Dzhumbus",
            List.of(), "Dobrovolcheskaya St.,12, Moscow 109004 Russia");
    public static final Restaurant restaurant3 = new Restaurant(RESTAURANT3_ID, "Anderson",
            List.of(), "Rublevskoye Hwy., 28/1, Moscow 613310 Russia");
    public static final Restaurant restaurant4 = new Restaurant(RESTAURANT4_ID, "Anderson",
            List.of(), "Lva Tolstogo, 23/1 floor 2, Moscow 119021 Russia");
    public static final Restaurant restaurant5 = new Restaurant(RESTAURANT5_ID, "Bryanskiy Byk",
            List.of(), "Bldg. 1 Bolshaya Lubyanka St. 24/15, Moscow 101000 Russia");

    public static final List<Restaurant> allRestaurants = List.of(restaurant4, restaurant3, restaurant5, restaurant2, restaurant1);

    public static Restaurant getNew() {
        return new Restaurant(null, "New Restaurant", List.of(), "New address");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT1_ID, "Updated Restaurant", List.of(), "Updated address");
    }
}
