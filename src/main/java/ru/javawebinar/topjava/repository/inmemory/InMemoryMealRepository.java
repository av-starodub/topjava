package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private final Map<Integer, ConcurrentHashMap<Integer, Meal>> repository;
    private final AtomicInteger counter;

    @Autowired
    public InMemoryMealRepository() {
        this.repository = new ConcurrentHashMap<>();
        this.counter = new AtomicInteger(0);
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save: {}, userId={}", meal, userId);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
        }
        return repository.computeIfAbsent(userId, (id) -> new ConcurrentHashMap<>()).put(meal.getId(), meal);
    }

    @Override
    public boolean delete(int userId, int id) {
        log.info("delete: {}, userId={}", userId, id);
        Map<Integer, Meal> userMeals = getMeals(userId);
        return Objects.nonNull(userMeals) && Objects.nonNull(userMeals.remove(id));
    }

    @Override
    public Meal get(int userId, int id) {
        log.info("get: userId={}, id={}", userId, id);
        Map<Integer, Meal> userMeals = getMeals(userId);
        return Objects.nonNull(userMeals) ? userMeals.get(id) : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll: userId={}", userId);
        return sortByDateFiltered(userId, filter -> true);
    }

    @Override
    public List<Meal> getAllInRange(LocalDate start, LocalDate end, int userId) {
        log.info("getInRange: userId={}", userId);
        return sortByDateFiltered(userId, meal -> DateTimeUtil.isBetweenTwoDate(meal.getDate(), start, end));
    }

    private Map<Integer, Meal> getMeals(int userId) {
        return repository.get(userId);
    }

    private List<Meal> sortByDateFiltered(int userId, Predicate<Meal> filter) {
        Map<Integer, Meal> userMeals = getMeals(userId);
        if (Objects.nonNull(userMeals)) {
            return userMeals
                    .values()
                    .stream()
                    .filter(filter)
                    .sorted(Comparator.comparing(Meal::getDateTime, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
