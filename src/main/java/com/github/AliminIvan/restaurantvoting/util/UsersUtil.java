package com.github.AliminIvan.restaurantvoting.util;

import lombok.experimental.UtilityClass;
import com.github.AliminIvan.restaurantvoting.model.Role;
import com.github.AliminIvan.restaurantvoting.model.User;
import com.github.AliminIvan.restaurantvoting.to.UserTo;

@UtilityClass
public class UsersUtil {

    public static User createNewFromTo(UserTo userTo) {
        return new User(null, userTo.getName(), userTo.getEmail().toLowerCase(), userTo.getPassword(), Role.USER);
    }

    public static User updateFromTo(User user, UserTo userTo) {
        user.setName(userTo.getName());
        user.setEmail(userTo.getEmail().toLowerCase());
        user.setPassword(userTo.getPassword());
        return user;
    }
}