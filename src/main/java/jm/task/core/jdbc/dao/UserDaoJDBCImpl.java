package jm.task.core.jdbc.dao;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final String tableName;
    private final String DELETE;
    private final String CLEAN;
    private final String DROP;
    private final String INSERT;

    public UserDaoJDBCImpl(String tableName) {
        this.tableName = tableName;
        this.DELETE = "DELETE FROM " + tableName + " WHERE id = ?";
        this.CLEAN = "DELETE FROM " + tableName;
        this.DROP = "DROP TABLE IF EXISTS " + tableName;
        this.INSERT = "INSERT INTO " + tableName + " (name, last_name, age) VALUES (?, ?, ?)";
    }

    // создание таблицы
    public void createUsersTable() {
        String create = "CREATE TABLE " + tableName + "("
                + "id INTEGER PRIMARY KEY AUTO_INCREMENT,"
                + "name VARCHAR(30) NOT NULL,"
                + "last_name VARCHAR(30) NOT NULL,"
                + "age INT NOT NULL)";

        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(create);
            System.out.println("Таблица '" + tableName + "' успешно создана!");
        } catch (SQLException e) {
            System.err.println("Ошибка при создании таблицы: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // удаление таблицы
    public void dropUsersTable() {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(DROP);
            System.out.println("Таблица " + tableName + " успешно удалена!");
        } catch (SQLException e) {
            System.err.println("Ошибка при создании таблицы: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // добавление юзера
    public void saveUser(String name, String lastName, byte age) {
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setInt(3, age);
            System.out.println("Пользователь " + name + " добавлен");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

        // удаление по id
    public void removeUserById(long id) {
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            System.out.println("Удалено " + id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<User>();
        String getAll = "SELECT * FROM " + tableName;
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(getAll)) {

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setAge(resultSet.getByte("age"));
                users.add(user);
            }
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
             PreparedStatement preparedStatement = connection.prepareStatement(CLEAN)) {
            int rowsDeleted = preparedStatement.executeUpdate();
            System.out.println("Удалено " + rowsDeleted + " записей");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
