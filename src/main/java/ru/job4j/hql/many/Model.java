package ru.job4j.hql.many;

import javax.persistence.*;
import java.util.Objects;

/**
 * Класс-модель авто.
 * @author Aleksandr Kuznetsov.
 * @version 1.0
 */
@Entity
@Table(name = "models")
public class Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    public static Model of(String name) {
        Model model = new Model();
        model.name = name;
        return model;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Model: "
                + "id=" + id
                + ", name='" + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Model model = (Model) o;
        return id == model.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
