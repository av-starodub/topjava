package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.util.Arrays;

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
            MealsUtil.meals.forEach(mealRestController::create);
            mealRestController.get(1);
        }
    }
}
