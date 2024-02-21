package com.github.AliminIvan.restaurantvoting.web.voting;

import com.github.AliminIvan.restaurantvoting.model.Menu;
import com.github.AliminIvan.restaurantvoting.model.Restaurant;
import com.github.AliminIvan.restaurantvoting.repository.MenuRepository;
import com.github.AliminIvan.restaurantvoting.repository.RestaurantRepository;
import com.github.AliminIvan.restaurantvoting.util.JsonUtil;
import com.github.AliminIvan.restaurantvoting.web.AbstractControllerTest;
import com.github.AliminIvan.restaurantvoting.web.user.UserTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.github.AliminIvan.restaurantvoting.web.voting.DishTestData.NOT_FOUND;
import static com.github.AliminIvan.restaurantvoting.web.voting.MenuTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminMenuControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = AdminMenuController.REST_URL + '/';
    private static final String BY_RESTAURANT = "by-restaurant?restaurantId=";
    private static final String RESTAURANT_ID = "restaurantId=";
    private static final String BY_DATE = "by-date?date=";
    private static final String AND_DATE = "&date=";
    private static final String BY_RESTAURANT_AND_DATE = "by-restaurant-and-date?";

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + MENU1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(menuRepository.findById(MENU1_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Restaurant newRestaurant = RestaurantTestData.getNew();
        Restaurant restaurant = restaurantRepository.save(newRestaurant);
        Menu newMenu = new Menu(null, null, List.of());
        newMenu.setRestaurant(restaurant);
        Integer restaurantId = restaurant.getId();
        ResultActions action = perform(MockMvcRequestBuilders.post(AdminMenuController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(restaurantId)))
                .andExpect(status().isCreated());

        Menu created = MENU_MATCHER.readFromJson(action);
        Integer newId = created.getId();
        newMenu.setId(newId);
        MENU_MATCHER.assertMatch(menuRepository.getExisted(newId), newMenu);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void createDuplicate() throws Exception {
        Restaurant restaurant = restaurantRepository.getExisted(RestaurantTestData.RESTAURANT1_ID);
        Menu menu = new Menu(null, restaurant, List.of());
        perform(MockMvcRequestBuilders.post(AdminMenuController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(RestaurantTestData.RESTAURANT1_ID)))
                .andDo(print())
                .andExpect(status().isConflict());
        assertThrows(DataIntegrityViolationException.class, () -> menuRepository.save(menu));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void update() throws Exception {
        LocalDate updateDate = LocalDate.now().minusDays(1);
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + MENU1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updateDate)))
                .andDo(print())
                .andExpect(status().isNoContent());

        MENU_MATCHER.assertMatch(menuRepository.getExisted(MENU1_ID), MenuTestData.getUpdated());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + MENU1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(menu1));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getAllByRestaurant() throws Exception {
        MvcResult result = perform(MockMvcRequestBuilders.get(REST_URL_SLASH + BY_RESTAURANT + RestaurantTestData.RESTAURANT1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        String expected = JsonUtil.writeValue(menusWithDishesForRestaurant1);
        assertEquals(expected, result.getResponse().getContentAsString());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getAllByDate() throws Exception {
        MvcResult result = perform(MockMvcRequestBuilders.get(REST_URL_SLASH + BY_DATE + NEW_YEAR))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        menusWithDishesForRestaurant2.get(0).setLunchDate(NEW_YEAR);
        String expected = JsonUtil.writeValue(menusWithDishesForRestaurant2);
        assertEquals(expected, result.getResponse().getContentAsString());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getByRestaurantAndDate() throws Exception {
        MvcResult result = perform(MockMvcRequestBuilders.get(REST_URL_SLASH + BY_RESTAURANT_AND_DATE +
                RESTAURANT_ID + RestaurantTestData.RESTAURANT1_ID + AND_DATE + LocalDate.now()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        String expected = JsonUtil.writeValue(menusWithDishesForRestaurant1.get(0));
        assertEquals(expected, result.getResponse().getContentAsString());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + MENU1_ID))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + MENU1_ID))
                .andExpect(status().isForbidden());
    }
}