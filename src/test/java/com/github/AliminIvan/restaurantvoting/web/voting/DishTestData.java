package com.github.AliminIvan.restaurantvoting.web.voting;

import com.github.AliminIvan.restaurantvoting.model.Dish;
import com.github.AliminIvan.restaurantvoting.web.MatcherFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DishTestData {
    public static final MatcherFactory.Matcher<Dish> DISH_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Dish.class, "menu");
    public static final int DISH1_ID = 1;
    public static final int DISH2_ID = 2;
    public static final int DISH3_ID = 3;
    public static final int DISH4_ID = 4;
    public static final int DISH5_ID = 5;
    public static final int DISH6_ID = 6;
    public static final int DISH7_ID = 7;
    public static final int DISH8_ID = 8;
    public static final int DISH9_ID = 9;
    public static final int DISH10_ID = 10;
    public static final int DISH11_ID = 11;
    public static final int DISH12_ID = 12;
    public static final int DISH13_ID = 13;
    public static final int DISH14_ID = 14;
    public static final int DISH15_ID = 15;
    public static final int NOT_FOUND = 20;
    public static final Dish dish1 = new Dish(DISH1_ID, "Салат с овощами", getRightFormatForPrice(200));
    public static final Dish dish2 = new Dish(DISH2_ID, "Харчо", getRightFormatForPrice(300));
    public static final Dish dish3 = new Dish(DISH3_ID, "Бризоль из курицы", getRightFormatForPrice(350));
    public static final Dish dish4 = new Dish(DISH4_ID, "Салат Цезарь", getRightFormatForPrice(350));
    public static final Dish dish5 = new Dish(DISH5_ID, "Борщ с говядиной", getRightFormatForPrice(350));
    public static final Dish dish6 = new Dish(DISH6_ID, "Паста Арабьятта", getRightFormatForPrice(380));
    public static final Dish dish7 = new Dish(DISH7_ID, "Винегрет", getRightFormatForPrice(200));
    public static final Dish dish8 = new Dish(DISH8_ID, "Суп гороховый", getRightFormatForPrice(280));
    public static final Dish dish9 = new Dish(DISH9_ID, "Гречка по-купечески", getRightFormatForPrice(300));
    public static final Dish dish10 = new Dish(DISH10_ID, "Салат оливье", getRightFormatForPrice(250));
    public static final Dish dish11 = new Dish(DISH11_ID, "Уха по-фински", getRightFormatForPrice(400));
    public static final Dish dish12 = new Dish(DISH12_ID, "Котлеты пожарские", getRightFormatForPrice(320));
    public static final Dish dish13 = new Dish(DISH13_ID, "Морковь по-корейски", getRightFormatForPrice(250));
    public static final Dish dish14 = new Dish(DISH14_ID, "Том Ям", getRightFormatForPrice(330));
    public static final Dish dish15 = new Dish(DISH15_ID, "Вок терияки с курицей", getRightFormatForPrice(300));

    private static BigDecimal getRightFormatForPrice(long value) {
        BigDecimal price = BigDecimal.valueOf(value);
        return price.setScale(2, RoundingMode.HALF_EVEN);
    }

    public static Dish getNew() {
        return new Dish(null, "New dish", getRightFormatForPrice(100));
    }

    public static Dish getUpdated() {
        return new Dish(DISH1_ID, "Updated dish", getRightFormatForPrice(100));
    }
}
