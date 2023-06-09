package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userNameB", "emailB@mail.ru", "password", Role.ADMIN));
            adminUserController.create(new User(null, "userNameA", "emailA@mail.ru", "password", Role.ADMIN));
            adminUserController.get(1);
            System.out.println(adminUserController.getByMail("emailB@mail.ru"));
            System.out.println(adminUserController.getAll());
            adminUserController.update(new User(1, "userNameB", "emailB2@mail.ru", "password", Role.ADMIN),1);
            System.out.println(adminUserController.get(1));
            //System.out.println(adminUserController.getByMail("emailB@mail.ru"));
            System.out.println(adminUserController.getAll());
        }
    }
}
