package org.diplom.accounting_app.models;

import io.ebean.Model;
import io.ebean.annotation.NotNull;
import jakarta.persistence.*;

@Entity
@Table(name = "Categories")
public class Category extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String name;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private User user;

    public Category(User user, String name) {
        this.user = user;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString(){
        return name;
    }
}
