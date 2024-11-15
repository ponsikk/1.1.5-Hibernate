package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();


        userService.createUsersTable();

        // Добавляем пользователей
        userService.saveUser("Иван", "Иванов", (byte) 25);
        userService.saveUser("Петр", "Петров", (byte) 30);
        userService.saveUser("Сергей", "Сергеев", (byte) 35);
        userService.saveUser("Алексей", "Алексеев", (byte) 28);


        userService.getAllUsers().forEach(user -> System.out.println(user));


        userService.cleanUsersTable();


        userService.dropUsersTable();
    }
}

