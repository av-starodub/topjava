package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int NOT_FOUND = 10;

    public static final Meal U_BREAKFAST = new Meal(START_SEQ + 2, LocalDateTime.of(2023, Month.JUNE, 22, 8, 0), "завтрак", 1000);
    public static final Meal U_LUNCH = new Meal(START_SEQ + 3, LocalDateTime.of(2023, Month.JUNE, 22, 8, 0), "завтрак", 1000);
    public static final Meal U_DINNER = new Meal(START_SEQ + 4, LocalDateTime.of(2023, Month.JUNE, 22, 8, 0), "завтрак", 1000);
    public static final Meal A_BREAKFAST = new Meal(START_SEQ + 5, LocalDateTime.of(2023, Month.JUNE, 22, 8, 0), "завтрак", 1000);
    public static final Meal A_LUNCH = new Meal(START_SEQ + 6, LocalDateTime.of(2023, Month.JUNE, 22, 8, 0), "завтрак", 1000);
    public static final Meal A_DINNER = new Meal(START_SEQ + 7, LocalDateTime.of(2023, Month.JUNE, 22, 8, 0), "завтрак", 1000);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2023, Month.JUNE, 23, 8, 0), "завтрак", 1000);
    }

    public static Meal getNewWithExistingDataTime() {
        return new Meal(null, LocalDateTime.of(2023, Month.JUNE, 22, 8, 0), "завтрак", 1000);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(U_BREAKFAST);
        updated.setDateTime(LocalDateTime.of(2023, Month.JUNE, 22, 8, 30));
        updated.setDescription("updated breakfast");
        updated.setCalories(1100);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }
}
/*
               INSERT INTO meals (user_id, date_time, description, calories)
               VALUES
               (100000, '2023-06-22 8:00:00', 'завтрак', 1000),
               (100000, '2023-06-22 13:00:00', 'обед', 1000),
               (100000, '2023-06-22 19:00:00', 'ужин', 500),
               (100001, '2023-06-22 10:00:00', 'завтрак', 500),
               (100001, '2023-06-22 14:00:00', 'обед', 1000),
               (100001, '2023-06-22 20:00:00', 'ужин', 500);
*/
