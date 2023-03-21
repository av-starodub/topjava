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
    private final AtomicInteger uniqueID;

    public MealMemoryStorage() {
        storage = new ConcurrentHashMap<>();
        uniqueID = new AtomicInteger(0);
        MealsUtil.fillTestStorage(this);
    }

    @Override
    public Meal get(int id) {
        return storage.get(id);
    }

    @Override
    public Meal add(Meal meal) {
        Objects.requireNonNull(meal, "Meal must not be null");
        meal.setId(uniqueID.incrementAndGet());
        return storage.put(meal.getId(), meal);
    }

    @Override
    public Meal update(Meal meal) {
        return storage.put(meal.getId(), meal);
    }

    @Override
    public Meal delete(int uniqueID) {
        return storage.remove(uniqueID);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(storage.values());
    }
}
