package ru.job4j.hql.many;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;
import java.util.List;

public class AutoHbmRun {
    public static void main(String[] args) {
        final Logger LOG = LogManager.getLogger(AutoHbmRun.class.getName());

        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            List<Model> models = new ArrayList<>();
            models.add(Model.of("X1"));
            models.add(Model.of("X3"));
            models.add(Model.of("M5"));
            models.add(Model.of("X5"));
            models.add(Model.of("i3"));
            models.forEach(session::save);

            Company company = Company.of("BMW");
            for (int i = 1; i <= 5; i++) {
                company.addModel(session.load(Model.class, i));
            }
            session.save(company);

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            LOG.trace("Exception", e);
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
