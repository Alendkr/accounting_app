package org.diplom.accounting_app.models;

import io.ebean.Model;
import io.ebean.annotation.NotNull;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "Receipts")
public class Receipt extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "UserID")
    private User user;

    private String descr;

    @NotNull
    private int amount;

    @NotNull
    private LocalDate receiptDate;

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

    public String getDescription() {
        return descr;
    }

    public void setDescription(String description) {
        this.descr = description;
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
}
