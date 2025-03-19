package org.diplom.accounting_app.models;

import io.ebean.Model;
import io.ebean.annotation.NotNull;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "Receipts")
public class Receipt extends Model {

    @Id
    private Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "UserID")
    private User user;

    private String descr;

    @NotNull
    private int amount;

    @NotNull
    @Column(name = "receipt_date")
    private LocalDate receiptDate;

    @ManyToOne
    @JoinColumn(name = "CategoryID")
    private Category category;

    public Receipt(User user, String descr, int amount, LocalDate receiptDate, Category category) {
        this.user = user;
        this.descr = descr;
        this.amount = amount;
        this.receiptDate = receiptDate;
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

    public LocalDate getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(LocalDate receiptDate) {
        this.receiptDate = receiptDate;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
