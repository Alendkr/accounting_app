package org.diplom.accounting_app.models;

import io.ebean.Model;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Expenses")
public class Expense extends Model {
    @Id
    private int id;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private User user;

    private String descr;
    private int amount;
    private LocalDate expenseDate;

    @ManyToOne
    @JoinColumn(name = "CategoryID")
    private Category category;

    public Expense(User user, String descr, int amount, LocalDate expenseDate, Category category) {
        this.user = user;
        this.descr = descr;
        this.amount = amount;
        this.expenseDate = expenseDate;
        this.category = category;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
