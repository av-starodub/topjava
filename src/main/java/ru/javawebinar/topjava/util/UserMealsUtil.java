package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public final class UserMealsUtil {
    private UserMealsUtil() {
    }

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
        Map<LocalDate, Integer> totalCaloriesByDate = new HashMap<>();
        meals.forEach(userMeal -> totalCaloriesByDate.merge(getDate(userMeal), userMeal.getCalories(), Integer::sum));

        List<UserMealWithExcess> mealsWithExcesses = new ArrayList<>();
        meals.forEach(userMeal -> {
                    if (TimeUtil.isBetweenHalfOpen(getTime(userMeal), startTime, endTime)) {
                        mealsWithExcesses.add(create(userMeal, totalCaloriesByDate.get(getDate(userMeal)) > caloriesPerDay));
                    }
                }
        );

        return mealsWithExcesses;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> totalCaloriesByDate = meals
                .stream()
                .collect(Collectors.groupingBy(UserMealsUtil::getDate, Collectors.summingInt(UserMeal::getCalories)));

        return meals
                .stream()
                .filter(userMeal -> TimeUtil.isBetweenHalfOpen(getTime(userMeal), startTime, endTime))
                .map(userMeal -> create(userMeal, totalCaloriesByDate.get(getDate(userMeal)) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static LocalDate getDate(UserMeal meal) {
        return LocalDate.from(meal.getDateTime());
    }

    private static LocalTime getTime(UserMeal meal) {
        return meal.getDateTime().toLocalTime();
    }

    private static UserMealWithExcess create(UserMeal userMeal, boolean excess) {
        return new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), excess);
    }

    private static void printMeals(Collection<?> meals) {
        Objects.requireNonNull(meals).forEach(System.out::println);
    }
}
