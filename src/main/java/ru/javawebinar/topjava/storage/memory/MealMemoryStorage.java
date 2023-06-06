package ru.javawebinar.topjava.storage.memory;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.MealStorage;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealMemoryStorage implements MealStorage {
    private final ConcurrentMap<Integer, Meal> storage;
    private final AtomicInteger id;

    public MealMemoryStorage() {
        storage = new ConcurrentHashMap<>();
        id = new AtomicInteger(0);
        MealsUtil.fillTestStorage(this);
    }

    @Override
    public Meal get(int id) {
        return storage.get(id);
    }

    @Override
    public Meal add(Meal meal) {
        Objects.requireNonNull(meal, "Meal must not be null");
        int nextId = id.incrementAndGet();
        meal.setId(nextId);
        return storage.putIfAbsent(nextId, meal);
    }

    @Override
    public Meal update(Meal meal) {
        int id = meal.getId();
        return storage.replace(id, meal);
    }

    @Override
    public Meal delete(int id) {
        return storage.remove(id);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(storage.values());
    }
}
