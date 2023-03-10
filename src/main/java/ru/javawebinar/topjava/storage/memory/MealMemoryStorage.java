package ru.javawebinar.topjava.storage.memory;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.Storage;
import ru.javawebinar.topjava.testData.Meals;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public final class MealMemoryStorage implements Storage {
    private static volatile MealMemoryStorage instance;
    private final ConcurrentMap<Integer, Meal> storage;
    private final AtomicInteger uniqueID;

    private MealMemoryStorage() {
        storage = new ConcurrentHashMap<>();
        uniqueID = new AtomicInteger(0);
        Meals.fillTestStorage(this);
    }

    public static MealMemoryStorage getInstance() {
        MealMemoryStorage localeInstance = instance;
        if (Objects.isNull(localeInstance)) {
            synchronized (MealMemoryStorage.class) {
                localeInstance = instance;
                if (Objects.isNull(localeInstance)) {
                    instance = new MealMemoryStorage();
                }
            }
        }
        return instance;
    }

    @Override
    public Meal get(Integer id) {
        return storage.get(id);
    }

    @Override
    public void add(Meal meal) {
        Objects.requireNonNull(meal, "Meal must not be null");
        meal.setId(uniqueID.incrementAndGet());
        storage.put(meal.getId(), meal);
    }

    @Override
    public void update(Meal meal, Integer id) {
        meal.setId(id);
        storage.put(id, meal);
    }

    @Override
    public void delete(Integer uniqueID) {
        storage.remove(uniqueID);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(storage.values());
    }
}
