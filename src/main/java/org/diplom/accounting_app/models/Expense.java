package org.diplom.accounting_app.models;

public class Expense {
    private String description;
    private int amount;
    private String date;

    public Expense(String description, int amount, String date) {
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public int getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }
}
