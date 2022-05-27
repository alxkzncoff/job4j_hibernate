package ru.job4j.hql.hql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

public class HqlHbmRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();

            Candidate one = Candidate.of("Alex", "1 year", 1500);
            Candidate two = Candidate.of("Mike", "2 year", 2000);
            Candidate three = Candidate.of("John", "4 year", 4000);

            save(one, sf);
            save(two, sf);
            save(three, sf);
            for (Object row: selectAll(sf)) {
                System.out.println(row);
            }
            System.out.println(selectById(1, sf));
            for (Object row: selectByName("Mike", sf)) {
                System.out.println(row);
            }
            update("Luke", "7 years", 7000, 3, sf);
            System.out.println(selectById(3, sf));
            insert(1, 4000, sf);
            for (Object row: selectAll(sf)) {
                System.out.println(row);
            }
            for (int i = 1; i <= 3; i++) {
                delete(i, sf);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    /**
     * Метод добавляет данные в БД.
     * @param candidate Кандидат
     * @param sf SessionFactory
     */
    public static void save(Candidate candidate, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(candidate);
        session.getTransaction().commit();
        session.close();
    }

    /**
     * Метод возвращает все данные из БД.
     * @param sf SessionFactory
     * @return Список данных.
     */
    public static List selectAll(SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        List result = session.createQuery("from Candidate").list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    /**
     * Метод ищет данные по идентификационному номеру.
     * @param id идентификационный номер.
     * @param sf SessionFactory
     * @return найденные данные.
     */
    public static Object selectById(int id, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        Object result = session.createQuery("from Candidate c where c.id = :fId")
                .setParameter("fId", id).uniqueResult();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    /**
     * Метод ищет данные по имени.
     * @param name Имя.
     * @param sf SessionFactory
     * @return найденные данные.
     */
    public static List selectByName(String name, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        List result = session.createQuery("from Candidate c where c.name = :Name")
                .setParameter("Name", name).list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    /**
     * Метод обновляет данные в БД по идентификационному номеру.
     * @param name Новое имя.
     * @param exp Новый опыт.
     * @param salary Новая зарплата.
     * @param id идентификационный номер.
     * @param sf SessionFactory
     */
    public static void update(String name, String exp, int salary, int id, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery("update Candidate c set c.name = :newName, "
                        + "c.experience = :newExperience, c.salary = :newSalary where id = :fId")
                .setParameter("newName", name)
                .setParameter("newExperience", exp)
                .setParameter("newSalary", salary)
                .setParameter("fId", id)
                .executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    /**
     * Метод добавляет новые данные в БД на основе старых.
     * @param id идентификационный номер старых данных.
     * @param salary размер прибавляемой зарплаты.
     * @param sf SessionFactory.
     */
    public static void insert(int id, int salary, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery("insert into Candidate (name, experience, salary) "
                        + "select c.name, c.experience, c.salary + :AddSalary "
                        + "from Candidate c where c.id = :fId")
                .setParameter("fId", id)
                .setParameter("AddSalary", salary)
                .executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    /**
     * Метод удаляет данные из БД по идентификационному номеру.
     * @param id идентификационный номер.
     * @param sf SessionFactory.
     */
    public static void delete(int id, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery("delete from Candidate where id = :fId")
                .setParameter("fId", id)
                .executeUpdate();
        session.getTransaction().commit();
        session.close();
    }
}
