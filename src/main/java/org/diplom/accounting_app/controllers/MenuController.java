package org.diplom.accounting_app.controllers;

import io.ebean.DB;
import io.ebean.Query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.diplom.accounting_app.models.CurrentUser;
import org.diplom.accounting_app.models.Expense;
import org.diplom.accounting_app.models.Receipt;
import javafx.scene.chart.PieChart;
import org.diplom.accounting_app.services.FinanceService;
import org.diplom.accounting_app.services.TransactionService;
import org.diplom.accounting_app.services.PeriodService;

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
    private final PeriodService periodService = new PeriodService();

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
        loadAll();
    }


    private void loadReceipts() {
        Query<Receipt> query = DB.find(Receipt.class);
        ObservableList<TransactionItem> receipts = FXCollections.observableArrayList();
        for (Receipt receipt : query.where().eq("UserID", CurrentUser.getCurrentUser().getId()).findList()) {
            receipts.add(new TransactionItem(receipt.getAmount(), receipt.getReceiptDate().toString()));
        }
        transactionsTable.setItems(receipts);
    }

    private void loadExpenses() {
        Query<Expense> query = DB.find(Expense.class);
        ObservableList<TransactionItem> expenses = FXCollections.observableArrayList();
        for (Expense expense : query.where().eq("UserID", CurrentUser.getCurrentUser().getId()).findList()) {
            expenses.add(new TransactionItem(expense.getAmount(), expense.getExpenseDate().toString()));
        }
        transactionsTable.setItems(expenses);
    }

    private void loadAll(){
        Query<Expense> query1 = DB.find(Expense.class);
        Query<Receipt> query2 = DB.find(Receipt.class);
        ObservableList<TransactionItem> history = FXCollections.observableArrayList();
        for (Expense expense : query1.where().eq("UserID", CurrentUser.getCurrentUser().getId()).findList()){
            history.add(new TransactionItem(expense.getAmount() * -1, expense.getExpenseDate().toString()));
        }
        for (Receipt receipt : query2.where().eq("UserID", CurrentUser.getCurrentUser().getId()).findList()){
            history.add(new TransactionItem(receipt.getAmount(), receipt.getReceiptDate().toString()));
        }
        transactionsTable.setItems(history);
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
            } else if (currentTableState == TableState.ALL) {
                loadAll();
            }
            updateChart(); // Обновляем диаграмму
        }
    }
    @FXML
    private void periodButtonClick(){
        boolean periodAdded = periodService.showPeriodDialog();
        if (periodAdded) {
            if (currentTableState == TableState.RECEIPTS){
                loadReceipts();
            }
            else if (currentTableState == TableState.EXPENSES){
                loadExpenses();
            } else if (currentTableState == TableState.ALL) {
                loadAll();
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