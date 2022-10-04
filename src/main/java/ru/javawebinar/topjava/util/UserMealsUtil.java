package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        LocalTime startTime = LocalTime.of(7, 0);
        LocalTime endTime = LocalTime.of(12, 0);

        printMeals(filteredByCycles(meals, startTime, endTime, 2000));
        printMeals(filteredByStreams(meals, startTime, endTime, 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return new ArrayList<UserMealWithExcess>() {{
            Map<LocalDateTime, Integer> amountCaloriesPerDay = new HashMap<>();
            meals.forEach(userMeal -> amountCaloriesPerDay.computeIfAbsent(userMeal.getDateTime(), k -> +userMeal.getCalories()));

            meals.forEach(userMeal -> {
                        LocalDateTime dateTime = userMeal.getDateTime();
                        if (TimeUtil.isBetweenHalfOpen(dateTime.toLocalTime(), startTime, endTime)) {
                            add(create(userMeal, amountCaloriesPerDay.get(dateTime) > caloriesPerDay));
                        }
                    }
            );
        }};
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDateTime, Integer> calories = meals
                .stream()
                .collect(Collectors.groupingBy(UserMeal::getDateTime, Collectors.summingInt(UserMeal::getCalories)));

        return meals
                .stream()
                .filter(userMeal -> TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                .map(userMeal -> create(userMeal, calories.get(userMeal.getDateTime()) > caloriesPerDay)
                )
                .collect(Collectors.toList());
    }

    private static UserMealWithExcess create(UserMeal userMeal, boolean excess) {
        return new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), excess);
    }

    private static void printMeals(List<UserMealWithExcess> meals) {
        Objects.requireNonNull(meals).forEach(System.out::println);
    }
}
