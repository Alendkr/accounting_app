package org.diplom.accounting_app.services;

import io.ebean.DB;
import org.diplom.accounting_app.models.Expense;
import org.diplom.accounting_app.models.Receipt;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import org.diplom.accounting_app.models.CurrentUser;

import java.util.List;
import java.util.stream.Collectors;

public class FinanceService {

    public void addExpense(Expense expense) {
        DB.save(expense);
    }

    public void addReceipt(Receipt receipt) {
        DB.save(receipt);
    }
    // Получаем все расходы
    public List<Expense> getAllExpenses() {
        return DB.find(Expense.class).findList();
    }

    // Получаем все доходы
    public List<Receipt> getAllReceipts() {
        return DB.find(Receipt.class).findList();
    }


    // Метод для обновления диаграммы
    public void updatePieChart(PieChart financeChart) {
        if (CurrentUser.getCurrentUser() == null) {
            System.out.println("Ошибка: Текущий пользователь не найден.");
            return;
        }

        int userId = CurrentUser.getCurrentUser().getId();

        // Получаем сумму всех расходов
        double totalExpenses = getAllExpenses().stream()
                .filter(expense -> expense.getUser().getId() == userId)
                .mapToDouble(Expense::getAmount)
                .sum();

        // Получаем сумму всех доходов
        double totalReceipts = getAllReceipts().stream()
                .filter(receipt -> receipt.getUser().getId() == userId)
                .mapToDouble(Receipt::getAmount)
                .sum();

        financeChart.getData().clear();
        financeChart.getData().add(new PieChart.Data("Доходы", totalReceipts));
        financeChart.getData().add(new PieChart.Data("Расходы", totalExpenses));
    }
}