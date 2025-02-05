package org.diplom.accounting_app.models;

import jakarta.persistence.Entity;
import java.time.LocalDate;


@Entity
public class Transaction {
    private int id;
    private int userId;
    private int amount;
    private LocalDate date;
    private String description;
    private boolean isExpense; // true — расход, false — доход

    public Transaction(int id, int userId, int amount, LocalDate date, String description, boolean isExpense) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.isExpense = isExpense;
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isExpense() {
        return isExpense;
    }

    public void setExpense(boolean expense) {
        isExpense = expense;
    }
}
