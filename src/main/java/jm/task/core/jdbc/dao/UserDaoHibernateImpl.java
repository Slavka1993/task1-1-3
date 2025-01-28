package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.Connection;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private final String drop;

    public UserDaoHibernateImpl() {
        this.drop = "DROP TABLE " + Util.TABLE_NAME;

    }


    @Override
    public void createUsersTable() {

        Transaction transaction = null;

        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            String createTable = "CREATE TABLE " + Util.TABLE_NAME + "("
                    + "id BIGINT PRIMARY KEY AUTO_INCREMENT,"
                    + "testName VARCHAR(30) NOT NULL,"
                    + "testLastName VARCHAR(30) NOT NULL,"
                    + "testAge TINYINT NOT NULL)";

            session.createSQLQuery(drop).executeUpdate();
            session.createSQLQuery(createTable).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
        System.out.println("Таблица успешно создана!");
    }

    @Override
    public void dropUsersTable() {
        Transaction transaction = null;

        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery(drop).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
        System.out.println("Таблица успешно удалена!");
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = new User();
            user.setName(name);
            user.setLastName(lastName);
            user.setAge(age);
            session.save(user);
            transaction.commit();
        } catch (Exception e) {
            throw new RuntimeException("User не был добавлен");
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            session.delete(user);
            transaction.commit();
        }
    }

    @Override
    public List<User> getAllUsers() {
        Transaction transaction = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            List<User> users = session.createSQLQuery(drop).list();
            transaction.commit();
        }
        return null;
    }

    @Override
    public void cleanUsersTable() {

    }
}
