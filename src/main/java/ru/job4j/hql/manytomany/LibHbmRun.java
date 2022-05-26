package ru.job4j.hql.manytomany;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;


public class LibHbmRun {
    public static void main(String[] args) {
        final Logger LOG = LogManager.getLogger(LibHbmRun.class.getName());

        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Book one = Book.of("No Time To Dragons");
            Book two = Book.of("Night watch");
            Book three = Book.of("When the world changed");

            Author first = Author.of("Nick Perumov");
            first.addBook(one);
            first.addBook(three);

            Author second = Author.of("Sergei Lukyanenko");
            second.addBook(one);
            second.addBook(two);

            session.persist(first);
            session.persist(second);

            Author author = session.get(Author.class, 1);
            session.remove(author);

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            LOG.trace("Exception", e);
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
