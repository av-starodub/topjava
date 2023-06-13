package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.inmemory.InMemoryUserRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.util.Arrays;
import java.util.Objects;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));

            // Check that the application layers for the "User" functionality are correctly added to the Spring context
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "emailB@mail.ru", "password", Role.ADMIN));
            adminUserController.get(1);

            // Check that the application layers for the "Meal" functionality are correctly added to the Spring context
            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            mealRestController.get(1);

            // Test email update
            adminUserController.update(new User(1, "userName", "NEW@mail.ru", "password", Role.ADMIN), 1);
            try {
                adminUserController.getByMail("emailB@mail.ru");
            } catch (NotFoundException e) {
                User user = adminUserController.get(1);
                System.out.println();
                System.out.println("TEST: CheckEmailUpdate");
                System.out.println("Email updated: " + "NEW@mail.ru".equals(user.getEmail()));
                System.out.println("Expected NotFoundException :" + "Not found entity with email=emailB@mail.ru".equals(e.getMessage()));
                System.out.println();
            }

            InMemoryUserRepository inMemoryUserRepository = appCtx.getBean(InMemoryUserRepository.class);
            User actual = inMemoryUserRepository.getByEmail("emailB@mail.ru");
            System.out.println();
            System.out.println("TEST: CheckThatOldEmailRemoved");
            System.out.println("Old email to user id relationship removed from relationship table: " + Objects.isNull(actual));
            System.out.println();
        }
    }
}
