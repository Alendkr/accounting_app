package org.diplom.accounting_app.models;

import jakarta.persistence.Entity;
import java.time.LocalDate;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Transaction {
    private int id;
    private int userId;
    private int amount;
    private LocalDate date;
    private String description;
    private boolean isExpense; // true — расход, false — доход

    public boolean isExpense() {
        return isExpense;
    }

}
