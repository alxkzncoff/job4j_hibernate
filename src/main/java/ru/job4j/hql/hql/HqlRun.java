package ru.job4j.hql.hql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HqlRun {
    public static void main(String[] args) {
        Candidate rsl = null;

        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();

            Session session = sf.openSession();
            session.beginTransaction();

            Candidate candidate = Candidate.of("Alex", "1 year", 1500);
            Vacancy vacancy = Vacancy.of("Java", "Java Developer");
            VacanciesDB db = VacanciesDB.of("Developers");
            db.addVacancy(vacancy);
            candidate.setDb(db);

            session.save(vacancy);
            session.save(db);
            session.save(candidate);

            rsl = session.createQuery(
                    "select distinct c from Candidate c "
                            + "join fetch c.db d "
                            + "join fetch d.vacancies v "
                            + "where c.id = :cId", Candidate.class
            ).setParameter("cId", 1).uniqueResult();

            session.getTransaction().commit();
            session.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }

        System.out.println(rsl);
    }
}
