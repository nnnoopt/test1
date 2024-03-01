package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {

        final String createUsersQuery = """
                CREATE SCHEMA IF NOT EXISTS public;
                CREATE TABLE IF NOT EXISTS public.USERTABLE (
                id BIGSERIAL PRIMARY KEY,
                name VARCHAR(255),
                lastname VARCHAR(255),
                age SMALLINT
                );
                """;

        try (var connection = Util.getConnection();
             var statement = connection.prepareStatement(createUsersQuery)) {
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при создании таблицы", e);
        }
    }

    public void dropUsersTable() {
        final String dropUsersQuery = "DROP TABLE IF EXISTS public.USERTABLE";

        try (var connection = Util.getConnection();
             var statement = connection.prepareStatement(dropUsersQuery)) {
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении таблицы", e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        final String saveUserQuery = "INSERT INTO public.USERTABLE (name, lastname, age) VALUES (?, ?, ?)";

        try (var connection = Util.getConnection();
             var preparedStatement = connection.prepareStatement(saveUserQuery)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении пользователя", e);
        }
    }

    public void removeUserById(long id) {
        final String removeUserQuery = "DELETE FROM public.USERTABLE WHERE id = ?";

        try (var connection = Util.getConnection();
             var preparedStatement = connection.prepareStatement(removeUserQuery)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении пользователя", e);
        }
    }

    public List<User> getAllUsers() {
        final String getAllUsersQuery = "SELECT * FROM public.USERTABLE";
        List<User> users = new ArrayList<>();

        try (var connection = Util.getConnection();
             var statement = connection.createStatement();
             var resultSet = statement.executeQuery(getAllUsersQuery)) {

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastname"));
                user.setAge(resultSet.getByte("age"));
                users.add(user);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении пользователей", e);
        }
        return users;
    }

    public void cleanUsersTable() {
        final String cleanUsersQuery = "TRUNCATE TABLE public.USERTABLE";

        try (var connection = Util.getConnection();
             var statement = connection.prepareStatement(cleanUsersQuery)) {
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при очистке таблицы", e);
        }
    }
}
