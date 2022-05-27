package ru.job4j.hql.lazy;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LazyHbmRun {
    public static void main(String[] args) {
        final Logger LOG = LogManager.getLogger(LazyHbmRun.class.getName());

        List<Company> list = new ArrayList<>();
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Company one = Company.of("BMW");
            Company two = Company.of("Mercedes");
            session.save(one);
            session.save(two);

            List<Model> bmwModels = Arrays.asList(
                    Model.of("X1", one),
                    Model.of("X3", one),
                    Model.of("M5", one),
                    Model.of("X5", one),
                    Model.of("i3", one));
            bmwModels.forEach(session::save);

            List<Model> mercModels = Arrays.asList(
                    Model.of("A-class", two),
                    Model.of("B-class", two),
                    Model.of("C-class", two),
                    Model.of("E-class", two),
                    Model.of("G-class", two));
            mercModels.forEach(session::save);

            list = session.createQuery(
                    "select distinct c from Company c join fetch c.models"
            ).list();
            session.getTransaction().commit();
            session.close();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
        for (Model model: list.get(0).getModels()) {
            LOG.debug("Result: " + model);
        }
        for (Model model: list.get(1).getModels()) {
            LOG.debug("Result: " + model);
        }
    }
}