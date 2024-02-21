package com.github.AliminIvan.restaurantvoting.web.voting;

import com.github.AliminIvan.restaurantvoting.model.Menu;
import com.github.AliminIvan.restaurantvoting.model.Restaurant;
import com.github.AliminIvan.restaurantvoting.model.Vote;
import com.github.AliminIvan.restaurantvoting.web.MatcherFactory;
import com.github.AliminIvan.restaurantvoting.web.user.UserTestData;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class VoteTestData {
    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Vote.class, "user");
    public static final int USER_VOTE_ID4 = 4;
    public static final int GUEST_VOTE_ID2 = 2;
    public static final int GUEST_VOTE_ID5 = 5;

    public static final Restaurant restaurant4 = new Restaurant(RestaurantTestData.RESTAURANT4_ID, "Anderson",
            null, "Lva Tolstogo, 23/1 floor 2, Moscow 119021 Russia");
    public static final Vote userVote4 = new Vote(USER_VOTE_ID4, null, new Restaurant(RestaurantTestData.RESTAURANT2_ID, null, null, null),
            LocalDate.of(2023, 12, 31), LocalTime.of(10, 43, 0));

    public static final Vote guestVote2 = new Vote(GUEST_VOTE_ID2, null, new Restaurant(RestaurantTestData.RESTAURANT5_ID, null, null, null),
            LocalDate.now(), LocalTime.of(8, 41, 0));

    public static final Vote guestVote5 = new Vote(GUEST_VOTE_ID5, null, new Restaurant(RestaurantTestData.RESTAURANT4_ID, null, null, null),
            LocalDate.of(2023, 12, 31), LocalTime.of(9, 34, 0));

    public static final List<Vote> guestVotes = List.of(guestVote2, guestVote5);

    public static final Restaurant restaurant1WithMenu = new Restaurant(RestaurantTestData.RESTAURANT1_ID, RestaurantTestData.restaurant1.getName(),
            List.of(new Menu(MenuTestData.MENU1_ID, List.of(DishTestData.dish1, DishTestData.dish2, DishTestData.dish3))), RestaurantTestData.restaurant1.getAddress());

    public static final Restaurant restaurant2WithMenu = new Restaurant(RestaurantTestData.RESTAURANT2_ID, RestaurantTestData.restaurant2.getName(),
            List.of(new Menu(MenuTestData.MENU2_ID, List.of(DishTestData.dish4, DishTestData.dish5, DishTestData.dish6))), RestaurantTestData.restaurant2.getAddress());

    public static final Restaurant restaurant3WithMenu = new Restaurant(RestaurantTestData.RESTAURANT3_ID, RestaurantTestData.restaurant3.getName(),
            List.of(new Menu(MenuTestData.MENU3_ID, List.of(DishTestData.dish7, DishTestData.dish8, DishTestData.dish9))), RestaurantTestData.restaurant3.getAddress());

    public static final Restaurant restaurant4WithMenu = new Restaurant(RestaurantTestData.RESTAURANT4_ID, RestaurantTestData.restaurant4.getName(),
            List.of(new Menu(MenuTestData.MENU4_ID, List.of(DishTestData.dish10, DishTestData.dish11, DishTestData.dish12))), RestaurantTestData.restaurant4.getAddress());

    public static final Restaurant restaurant5WithMenu = new Restaurant(RestaurantTestData.RESTAURANT5_ID, RestaurantTestData.restaurant5.getName(),
            List.of(new Menu(MenuTestData.MENU5_ID, List.of(DishTestData.dish13, DishTestData.dish14, DishTestData.dish15))), RestaurantTestData.restaurant5.getAddress());

    public static final List<Restaurant> allRestaurantsWithMenus = List.of(restaurant4WithMenu, restaurant3WithMenu, restaurant5WithMenu,
            restaurant2WithMenu, restaurant1WithMenu);

    public static Vote getNew() {
        return new Vote(LocalDate.now(), LocalTime.now().truncatedTo(ChronoUnit.SECONDS), UserTestData.user, restaurant4);
    }

    public static Vote getUpdated() {
        return new Vote(GUEST_VOTE_ID2, UserTestData.user, restaurant4, LocalDate.now(), LocalTime.now().truncatedTo(ChronoUnit.SECONDS));
    }
}
