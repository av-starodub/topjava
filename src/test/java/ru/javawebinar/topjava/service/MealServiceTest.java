package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.MealTestData.NOT_FOUND;


@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))

public class MealServiceTest {
    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void create() {
        Meal created = service.create(getNew(), USER_ID);
        int createdId = created.getId();
        Meal expected = getNew();
        expected.setId(createdId);
        assertMatch(created, expected);
    }

    @Test
    public void createWithSameTime() {
        Meal created = getNewWithExistingDataTime();
        assertThrows(DuplicateKeyException.class, () -> service.create(created, USER_ID));
    }

    @Test
    public void get() {
        assertMatch(service.get(U_BREAKFAST.getId(), USER_ID), U_BREAKFAST);
    }

    @Test
    public void getNotOwn() {
        assertThrows(NotFoundException.class, () -> service.get(U_BREAKFAST.getId(), ADMIN_ID));
    }

    @Test
    public void getNotExist() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, USER_ID));
    }


    @Test
    public void delete() {
        service.delete(A_BREAKFAST.getId(), ADMIN_ID);
        assertThrows(NotFoundException.class, () -> service.get(A_BREAKFAST.getId(), ADMIN_ID));
    }

    @Test
    public void deleteNotOwn() {
        assertThrows(NotFoundException.class, () -> service.delete(U_BREAKFAST.getId(), ADMIN_ID));
    }

    @Test
    public void deleteNotExist() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, USER_ID));
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(U_BREAKFAST.getId(), USER_ID), getUpdated());
    }

    @Test
    public void updateNotOwn() {
        Meal updated = getUpdated();
        assertThrows(NotFoundException.class, () -> service.update(updated, ADMIN_ID));
    }

    @Test
    public void getAll() {
        List<Meal> meals = service.getAll(USER_ID);
        assertMatch(meals, U_DINNER, U_LUNCH, U_BREAKFAST);
    }

    @Test
    public void getAllBetweenInclusive() {
        List<Meal> meals = service.getBetweenInclusive(
                LocalDate.of(2023, Month.JUNE, 22), null, ADMIN_ID
        );
        assertMatch(meals, A_DINNER, A_LUNCH, A_BREAKFAST);
    }
}
