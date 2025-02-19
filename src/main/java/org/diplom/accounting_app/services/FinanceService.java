package org.diplom.accounting_app.services;

import io.ebean.DB;
import javafx.util.Pair;
import org.diplom.accounting_app.models.Expense;
import org.diplom.accounting_app.models.Receipt;
import org.diplom.accounting_app.models.User;
import org.diplom.accounting_app.models.CurrentUser;
import java.util.List;

public class FinanceService {

    public List<Expense> loadExpenses() {
        User currentUser = CurrentUser.getCurrentUser();
        if (currentUser == null) {
            return List.of();
        }
        return DB.find(Expense.class).where().eq("user", currentUser).findList();
    }

    public List<Receipt> loadReceipts() {
        User currentUser = CurrentUser.getCurrentUser();
        if (currentUser == null) {
            return List.of();
        }
        return DB.find(Receipt.class).where().eq("user", currentUser).findList();
    }

    public Pair<Integer, Integer> getFinanceSummary() {
        User currentUser = CurrentUser.getCurrentUser();
        if (currentUser == null) {
            return new Pair<>(0, 0);
        }

        // Оптимизированный запрос без загрузки всех объектов в память
        Integer totalExpenses = DB.find(Expense.class)
                .where().eq("user", currentUser)
                .select("sum(amount)")
                .findSingleAttribute();

        Integer totalReceipts = DB.find(Receipt.class)
                .where().eq("user", currentUser)
                .select("sum(amount)")
                .findSingleAttribute();

        return new Pair<>(totalReceipts != null ? totalReceipts : 0,
                totalExpenses != null ? totalExpenses : 0);
    }

//    public void addExpense(Expense expense) {
//        User currentUser = CurrentUser.getCurrentUser();
//        if (currentUser != null) {
//            expense.setUser(currentUser);
//            DB.save(expense);
//        }
//    }
//
//    public void addReceipt(Receipt receipt) {
//        User currentUser = CurrentUser.getCurrentUser();
//        if (currentUser != null) {
//            receipt.setUser(currentUser);
//            DB.save(receipt);
//        }
//    }
}
