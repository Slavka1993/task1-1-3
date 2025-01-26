package jm.task.core.jdbc.dao;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private final String delete;
    private final String clean;
    private final String drop;
    private final String insert;

    public UserDaoJDBCImpl() {
        this.delete = "DELETE FROM " + Util.TABLE_NAME + " WHERE id = ?";
        this.clean = "DELETE FROM " + Util.TABLE_NAME;
        this.drop = "DROP TABLE IF EXISTS " + Util.TABLE_NAME;
        this.insert = "INSERT INTO " + Util.TABLE_NAME + " (testName, testLastname, testAge) VALUES (?, ?, ?)";
    }

    // создание таблицы
    public void createUsersTable() {
        String create = "CREATE TABLE " + Util.TABLE_NAME + "("
                + "id BIGINT PRIMARY KEY AUTO_INCREMENT,"
                + "testName VARCHAR(30) NOT NULL,"
                + "testLastName VARCHAR(30) NOT NULL,"
                + "testAge TINYINT NOT NULL)";

        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(create);
            System.out.println("Таблица '" + Util.TABLE_NAME + "' успешно создана!");
        } catch (SQLException e) {
            System.err.println("Ошибка при создании таблицы: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // удаление таблицы
    public void dropUsersTable() {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(drop);
            System.out.println("Таблица " + Util.TABLE_NAME + " успешно удалена!");
        } catch (SQLException e) {
            System.err.println("Ошибка при создании таблицы: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // добавление юзера
    public void saveUser(String name, String lastName, byte age) {
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insert)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            System.out.println("Пользователь " + name + " добавлен");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // удаление по id
    public void removeUserById(long id) {
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(delete)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            System.out.println("Удалено " + id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String getAll = "SELECT testName, testLastName, testAge FROM " + Util.TABLE_NAME;
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(getAll)) {

            while (resultSet.next()) {
                User user = new User();
                user.setName(resultSet.getString("testName"));
                user.setLastName(resultSet.getString("testLastName"));
                user.setAge(resultSet.getByte("testAge"));
                users.add(user);
            }
            System.out.println(users.size());

        } catch (SQLException e) {
            System.err.println("Ошибка при получении пользователей: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("Получены пользователи: " + users);
        return users;
    }

    //Очистка таблицы
    public void cleanUsersTable() {
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(clean)) {
            int rowsDeleted = preparedStatement.executeUpdate();
            System.out.println("Удалено " + rowsDeleted + " записей");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
