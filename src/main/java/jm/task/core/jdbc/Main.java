package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoHibernateImpl;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;
import org.hibernate.CustomEntityDirtinessStrategy;

import java.util.List;


public class Main {

    public static void main(String[] args) {
//        UserDao userDao = new UserDaoJDBCImpl();
//        UserService userService = new UserServiceImpl(userDao);

        UserDao userDao = new UserDaoHibernateImpl();
        UserService userService = new UserServiceImpl(userDao);

        userService.createUsersTable();

        userService.saveUser("Вячеслав", "Черенков", (byte) 31);
        userService.saveUser("Николай", "Иванов", (byte) 28);
        userService.saveUser("Иван", "Козлов", (byte) 25);
        userService.saveUser("Владимир", "Удалов", (byte) 31);

        userService.getAllUsers();

        userService.removeUserById(1);
        userService.createUsersTable();
        userService.dropUsersTable();
    }


}
