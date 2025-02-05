package org.diplom.accounting_app.models;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

//@Entity
//@Table(name = "Receipts")
//@Getter
//@Setter
//@AllArgsConstructor
public class Receipt {
//    @Id
//    private int id;

    private String description;
    private int amount;
    private String date;

//    @ManyToOne(optional = false)
//    User user;

}
