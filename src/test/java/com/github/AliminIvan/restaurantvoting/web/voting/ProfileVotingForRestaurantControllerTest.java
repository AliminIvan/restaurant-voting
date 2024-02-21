package com.github.AliminIvan.restaurantvoting.web.voting;

import com.github.AliminIvan.restaurantvoting.model.Vote;
import com.github.AliminIvan.restaurantvoting.repository.VoteRepository;
import com.github.AliminIvan.restaurantvoting.util.DateTimeUtil;
import com.github.AliminIvan.restaurantvoting.util.JsonUtil;
import com.github.AliminIvan.restaurantvoting.web.AbstractControllerTest;
import com.github.AliminIvan.restaurantvoting.web.user.UserTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;
import org.junit.jupiter.api.condition.EnabledIf;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static com.github.AliminIvan.restaurantvoting.util.DateTimeUtil.DEAD_LINE_TIME;
import static com.github.AliminIvan.restaurantvoting.web.voting.VoteTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileVotingForRestaurantControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = ProfileVotingForRestaurantController.REST_URL + '/';
    private static final String BY_DATE = "by-date?date=";
    private static final String WITH_MENU = "/restaurant-with-menu";
    private static final String WITH_MENUS = "restaurants-with-menus";

    @Autowired
    private VoteRepository voteRepository;

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + USER_VOTE_ID4))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(userVote4));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + DishTestData.NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = UserTestData.GUEST_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(ProfileVotingForRestaurantController.REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(guestVotes));
    }

    @Test
    @WithUserDetails(value = UserTestData.GUEST_MAIL)
    void getByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + BY_DATE + LocalDate.now()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(guestVote2));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getRestaurantWithMenus() throws Exception {
        MvcResult result = perform(MockMvcRequestBuilders.get(REST_URL_SLASH + RestaurantTestData.RESTAURANT1_ID + WITH_MENU))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        String expected = JsonUtil.writeValue(restaurant1WithMenu);
        assertEquals(expected, result.getResponse().getContentAsString());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getRestaurantsWithMenus() throws Exception {
        MvcResult result = perform(MockMvcRequestBuilders.get(REST_URL_SLASH + WITH_MENUS))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        String expected = JsonUtil.writeValue(allRestaurantsWithMenus);
        assertEquals(expected, result.getResponse().getContentAsString());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void createWihLocation() throws Exception {
        Vote newVote = getNew();
        Integer restaurantId = newVote.getRestaurant().getId();
        ResultActions action = perform(MockMvcRequestBuilders.post(ProfileVotingForRestaurantController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(restaurantId)))
                .andExpect(status().isCreated());

        Vote created = VOTE_MATCHER.readFromJson(action);
        Integer newId = created.getId();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(created, newVote);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = UserTestData.GUEST_MAIL)
    void createDuplicate() throws Exception {
        Vote duplicate = new Vote(LocalDate.now(), LocalTime.now().truncatedTo(ChronoUnit.SECONDS), UserTestData.guest, RestaurantTestData.restaurant5);
        Integer restaurantId = duplicate.getRestaurant().getId();
        perform(MockMvcRequestBuilders.post(ProfileVotingForRestaurantController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(restaurantId)))
                .andDo(print())
                .andExpect(status().isConflict());
        assertThrows(DataIntegrityViolationException.class, () -> voteRepository.save(duplicate));
    }

    @Test
    @WithUserDetails(value = UserTestData.GUEST_MAIL)
    @DisabledIf("isAfterDeadLineTime")
    void update() throws Exception {
        Vote updated = getUpdated();
        Integer restaurantId = updated.getRestaurant().getId();
        perform(MockMvcRequestBuilders.put(ProfileVotingForRestaurantController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(restaurantId)))
                .andExpect(status().isNoContent());

        String actual = JsonUtil.writeValue(voteRepository.get(UserTestData.GUEST_ID, GUEST_VOTE_ID2).orElseThrow());
        assertEquals(JsonUtil.writeValue(updated), actual);
    }

    @Test
    @WithUserDetails(value = UserTestData.GUEST_MAIL)
    @EnabledIf("isAfterDeadLineTime")
    void updateAfterDeadLineTime() throws Exception {
        Vote updated = getUpdated();
        Integer restaurantId = updated.getRestaurant().getId();
        MvcResult result = perform(MockMvcRequestBuilders.put(ProfileVotingForRestaurantController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(restaurantId)))
                .andExpect(status().isConflict())
                .andReturn();
        String expected = String.format("User can`t change vote after %s o`clock", DEAD_LINE_TIME);
        String actual = result.getResponse().getContentAsString();
        assertTrue(actual.contains(expected));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + USER_VOTE_ID4))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    boolean isAfterDeadLineTime() {
        return LocalDateTime.now().toLocalTime().isAfter(DateTimeUtil.DEAD_LINE_TIME);
    }
}