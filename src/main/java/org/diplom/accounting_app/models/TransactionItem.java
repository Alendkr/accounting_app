package org.diplom.accounting_app.models;

public class TransactionItem {
    private int amount;
    private String date;
    private String category;  // Поле для категории

    // Конструктор с инициализацией всех полей
    public TransactionItem(int amount, String date, String category) {
        this.amount = amount;
        this.date = date;
        this.category = category;  // Инициализация категории
    }

    // Геттеры для всех полей
    public int getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    // Сеттеры (если нужно изменить значения после создания объекта)
    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "TransactionItem{" +
                "amount=" + amount +
                ", date='" + date + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
