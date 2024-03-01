package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;


public class Main {
    public static void main(String[] args) {

        UserService userService = new UserServiceImpl();
        userService.createUsersTable();
        userService.saveUser("Alex", "Alex", (byte) 22);
        userService.saveUser("Alla", "Alex", (byte) 22);
        userService.saveUser("Bella", "Alex", (byte) 22);
        userService.saveUser("Fox", "Alex", (byte) 22);
        userService.getAllUsers();
        userService.cleanUsersTable();
        userService.dropUsersTable();

    }
}
