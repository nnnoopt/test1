package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        Transaction transaction = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.createSQLQuery("CREATE SCHEMA IF NOT EXISTS public").executeUpdate();

            session.createSQLQuery("""
                    CREATE TABLE IF NOT EXISTS public.USERTABLE (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(50) NOT NULL,
                    lastName VARCHAR(50) NOT NULL,
                    age SMALLINT NOT NULL
                    )
                    """
            ).executeUpdate();

            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Ошибка при создании таблицы.", e);
        }
    }


    @Override
    public void dropUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            session.createSQLQuery("DROP TABLE IF EXISTS public.USERTABLE").executeUpdate();

            transaction.commit();
        } catch (HibernateException e) {
            throw new RuntimeException("Ошибка при удалении таблицы.", e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            session.save(new User(name, lastName, age));

            transaction.commit();

        } catch (HibernateException e) {
            throw new RuntimeException("Ошибка при добавлении пользователя.", e);
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            User user = session.get(User.class, id);

            if (user != null) {
                session.delete(user);
                transaction.commit();
            }

        } catch (HibernateException e) {
            throw new RuntimeException("Ошибка при удалении пользователя.", e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = Util.getSessionFactory().openSession()) {


            return session.createQuery("From User", User.class).list();

        } catch (HibernateException e) {
            throw new RuntimeException("Ошибка при получении пользователей.", e);
        }
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Query query = session.createQuery("DELETE FROM User");
            query.executeUpdate();

            transaction.commit();
        } catch (HibernateException e) {
            throw new RuntimeException("Ошибка при очищении таблицы.", e);
        }
    }
}
