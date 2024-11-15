package jm.task.core.jdbc.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;


import java.util.ArrayList;
import java.util.List;
//почему везде явный кастинг к (Transaction)? Idea обманула с подсказками, нужно было просто импорт сделать
public class UserDaoHibernateImpl implements UserDao {

    public UserDaoHibernateImpl() {
    }

    @Override
    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                "name VARCHAR(50), " +
                "lastName VARCHAR(50), " +
                "age TINYINT) ENGINE=MyISAM";

        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.createNativeQuery(sql).executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                handleRollback(transaction);  // Роллбэк
                e.printStackTrace();
            }
        }
    }

    @Override
    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS users";

        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.createNativeQuery(sql).executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                handleRollback(transaction);  // Роллбэк
                e.printStackTrace();
            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                User user = new User(name, lastName, age);
                session.save(user);
                transaction.commit();
                System.out.println("Пользователь с именем — " + name + " добавлен в базу данных");
            } catch (Exception e) {
                handleRollback(transaction);  // Роллбэк
                e.printStackTrace();
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        String hql = "DELETE FROM User WHERE id = :userId"; // Строго один запрос
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.createQuery(hql).setParameter("userId", id).executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                handleRollback(transaction); // Роллбэк
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Session session = Util.getSessionFactory().openSession()) {
            users = session.createQuery("from User", User.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    } // Роллбэк не нужен ?

    @Override
    public void cleanUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.createQuery("delete from User").executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                handleRollback(transaction); // Роллбэк
                e.printStackTrace();
            }
        }
    }

    private void handleRollback(Transaction transaction) {
        if (transaction != null) {
            try {
                transaction.rollback();
            } catch (RuntimeException rollbackException) {
                System.err.println("Ошибка при выполнении отката транзакции:");
                rollbackException.printStackTrace();
            }
        }
    }
}

