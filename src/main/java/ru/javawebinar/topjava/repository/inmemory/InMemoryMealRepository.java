package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);
    private final Map<Integer, ConcurrentHashMap<Integer, Meal>> repository;
    private final Map<Integer, Integer> permissions;
    private final AtomicInteger counter;

    @Autowired
    public InMemoryMealRepository() {
        this.repository = new ConcurrentHashMap<>();
        this.permissions = new ConcurrentHashMap<>();
        this.counter = new AtomicInteger(0);
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("{} save: {}, userId={}", LocalDateTime.now(), meal, userId);
        if (!meal.isNew() && !verifyAccess(meal.getId(), userId)) {
            return null;
        }
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            permissions.put(meal.getId(), userId);
        }
        return repository.computeIfAbsent(userId, (id) -> new ConcurrentHashMap<>()).put(meal.getId(), meal);
    }

    @Override
    public boolean delete(int userId, int id) {
        log.info("{} delete: {}, userId={}", LocalDateTime.now(), userId, id);
        if (!verifyAccess(id, userId)) {
            return false;
        }
        Map<Integer, Meal> userMeals = getMeals(userId);
        if (notExist(userMeals) || Objects.isNull(userMeals.remove(id))) {
            return false;
        }
        if (userMeals.isEmpty()) {
            clearUserData(userId);
        }
        return true;
    }

    @Override
    public Meal get(int userId, int id) {
        log.info("{} get: userId={}, id={}", LocalDateTime.now(), userId, id);
        return verifyAccess(id, userId) ? getMeals(userId).get(id) : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("{} getAll: userId={}", LocalDateTime.now(), userId);
        Map<Integer, Meal> userMeals = getMeals(userId);
        if (notExist(userMeals)) {
            return Collections.emptyList();
        }
        return sortByTime(sortByDate(userMeals));
    }

    private Map<Integer, Meal> getMeals(int userId) {
        return repository.get(userId);
    }

    private boolean verifyAccess(int id, int userId) {
        return permissions.get(id) == userId;
    }

    private boolean notExist(Map<Integer, Meal> meals) {
        return Objects.isNull(meals);
    }

    private void clearUserData(int userId) {
        repository.remove(userId);
        permissions.forEach((id, value) -> {
            if (value == userId) {
                permissions.remove(id);
            }
        });
    }

    private Map<LocalDate, List<Meal>> sortByDate(Map<Integer, Meal> userMeals) {
        SortedMap<LocalDate, List<Meal>> mealsByDate = new ConcurrentSkipListMap<>(DateTimeUtil::compareByDate);
        userMeals.values()
                .forEach(meal -> mealsByDate.computeIfAbsent(meal.getDate(), (localDate) -> new ArrayList<>()).add(meal)
                );
        return mealsByDate;
    }

    private List<Meal> sortByTime(Map<LocalDate, List<Meal>> userMealsByDate) {
        List<Meal> mealsByTime = new ArrayList<>();
        userMealsByDate.values()
                .forEach(list -> {
                    list.sort(Comparator.comparing(Meal::getTime, DateTimeUtil::compareByTime));
                    mealsByTime.addAll(list);
                });
        return mealsByTime;
    }
}
