package org.diplom.accounting_app.models;

public class TransactionItem {
    private final Integer amount;
    private final String date;

    public TransactionItem(Integer amount, String date) {
        this.amount = amount;
        this.date = date;
    }

    public Integer getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }
}
