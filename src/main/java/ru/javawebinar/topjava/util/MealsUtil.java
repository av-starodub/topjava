package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class MealsUtil {
    public static final int CALORIES_PER_DAY = 2000;

    private MealsUtil() {
    }

    public static List<MealTo> toFilteredListMealTo(List<Meal> meals, Predicate<Meal> filter, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumByDate = sumCaloriesByDate(meals);
        return meals.stream()
                .filter(filter)
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static List<MealTo> toListMealTo(List<Meal> meals, int caloriesPerDay) {
        return toFilteredListMealTo(meals, (meal -> true), caloriesPerDay);
    }

    private static Map<LocalDate, Integer> sumCaloriesByDate(List<Meal> meals) {
        return meals.stream()
                .collect(Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories)));
    }

    private static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal, excess);
    }
}
