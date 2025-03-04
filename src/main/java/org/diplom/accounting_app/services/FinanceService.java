package org.diplom.accounting_app.services;

import io.ebean.DB;
import org.diplom.accounting_app.models.Expense;
import org.diplom.accounting_app.models.Receipt;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import org.diplom.accounting_app.models.CurrentUser;
import org.diplom.accounting_app.models.TransactionItem;
import java.util.List;


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

    public void updatePieChartForPeriod(PieChart chart, List<TransactionItem> transactions) {
        int totalIncome = 0;
        int totalExpense = 0;

        for (TransactionItem item : transactions) {
            if (item.getAmount() > 0) {
                totalIncome += item.getAmount();
            } else {
                totalExpense += Math.abs(item.getAmount());
            }
        }

        ObservableList<PieChart.Data> chartData = FXCollections.observableArrayList(
                new PieChart.Data("Доходы", totalIncome),
                new PieChart.Data("Расходы", totalExpense)
        );

        chart.setData(chartData);
    }

}