package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;
@Service
public class MealService {

    private final MealRepository repository;
    @Autowired
    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal get(int userId, int id) {
        return checkNotFoundWithId(repository.get(userId, id), id);
    }

    public Meal create(Meal meal, int userId) {
        Objects.requireNonNull(meal);
        return repository.save(meal, userId);
    }

    public void update(Meal meal, int userId) {
        Objects.requireNonNull(meal);
        checkNotFoundWithId(repository.save(meal, userId), meal.getId());
    }

    public void delete(int userId, int id) {
        checkNotFoundWithId(repository.delete(userId, id), id);
    }

    public List<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }

    public Collection<Meal> filter(int userId, LocalDate startDate, LocalDate endDate) {
        return repository.getAllInRange(startDate, endDate, userId);
    }
}