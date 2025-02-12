package org.diplom.accounting_app.controllers;

import io.ebean.DB;
import io.ebean.Query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.diplom.accounting_app.models.Expense;
import org.diplom.accounting_app.models.Receipt;
import javafx.scene.chart.PieChart;
import org.diplom.accounting_app.services.FinanceService;
import org.diplom.accounting_app.services.TransactionService;

import java.util.List;

public class MenuController {




    private enum TableState {
        RECEIPTS, EXPENSES, ALL
    }

    private TableState currentTableState;

    @FXML
    private TableView<TransactionItem> transactionsTable;

    @FXML
    private TableColumn<TransactionItem, Double> amountColumn;

    @FXML
    private TableColumn<TransactionItem, String> dateColumn;

    @FXML
    private PieChart financeChart;

    private final FinanceService financeService = new FinanceService();
    private final TransactionService transactionService = new TransactionService();

    @FXML
    public void initialize() {
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        updateChart();
    }

    @FXML
    private void receiptButtonClick() {
        currentTableState = TableState.RECEIPTS;
        loadReceipts();
    }

    @FXML
    private void expenseButtonClick() {
        currentTableState = TableState.EXPENSES;
        loadExpenses();
    }
    @FXML
    private void historyButtonClick() {
        currentTableState = TableState.ALL;
    }


    private void loadReceipts() {
        Query<Receipt> query = DB.find(Receipt.class);
        ObservableList<TransactionItem> receipts = FXCollections.observableArrayList();
        for (Receipt receipt : query.findList()) {
            receipts.add(new TransactionItem(receipt.getAmount(), receipt.getReceiptDate().toString()));
        }
        transactionsTable.setItems(receipts);
    }

    private void loadExpenses() {
        Query<Expense> query = DB.find(Expense.class);
        ObservableList<TransactionItem> expenses = FXCollections.observableArrayList();
        for (Expense expense : query.findList()) {
            expenses.add(new TransactionItem(expense.getAmount(), expense.getExpenseDate().toString()));
        }
        transactionsTable.setItems(expenses);
    }

    @FXML
    private void addButtonClick() {
        boolean transactionAdded = transactionService.showTransactionDialog();
        if (transactionAdded) {
            if (currentTableState == TableState.RECEIPTS){
                loadReceipts();
            }
            else if (currentTableState == TableState.EXPENSES){
                loadExpenses();
            } else if (currentTableState == null) {
                updateChart();
            }
            updateChart(); // Обновляем диаграмму
        }
    }

    public static class TransactionItem {
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
    private void updateChart() {
        financeService.updatePieChart(financeChart);
    }
}