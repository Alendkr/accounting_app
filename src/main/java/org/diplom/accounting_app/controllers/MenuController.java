package org.diplom.accounting_app.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import org.diplom.accounting_app.models.Expense;
import org.diplom.accounting_app.models.Receipt;
import org.diplom.accounting_app.services.FinanceService;
import org.diplom.accounting_app.services.TransactionService;

public class MenuController {
//    @FXML
//    private TableView<Expense> expensesTable;
//    @FXML
//    private TableView<Receipt> receiptsTable;
    @FXML
    private TableView<?> transactionsTable;
//    @FXML
//    private TableColumn<Receipt, Integer> receiptAmountColumn;
    @FXML
    private TableColumn<Receipt, String> receiptDateColumn;
    @FXML
    private TableColumn<Expense, Integer> expenseAmountColumn;
    @FXML
   private TableColumn<Expense, String> expenseDateColumn;

    @FXML
    private PieChart financeChart;


    private final FinanceService financeService = new FinanceService();
    private final TransactionService transactionService = new TransactionService();


    @FXML
    public void initialize() {
        setupTableColumns();
        updateChart();
    }
    // Настройка столбцов таблиц
    private void setupTableColumns() {
        expenseAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        expenseDateColumn.setCellValueFactory(new PropertyValueFactory<>("expenseDate"));

    }

    // Обновление диаграммы
    private void updateChart() {
        financeService.updatePieChart(financeChart);
    }
    @FXML
    private void addButtonClick() {
        boolean transactionAdded = transactionService.showTransactionDialog();
        if (transactionAdded) {
            updateChart(); // Обновляем диаграмму
        }
    }
    public void receiptButtonClick() {
        transactionsTable.getColumns().clear();
        TableColumn<Receipt, Integer> receiptAmountColumn = new TableColumn<>("Сумма");
        receiptAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        TableColumn<Receipt, String> receiptDateColumn = new TableColumn<>("Дата");
        receiptDateColumn.setCellValueFactory(new PropertyValueFactory<>("receiptDate"));

        ObservableList<Receipt> receipts = financeService.loadReceipts();


    }


    public void expenseButtonClick(ActionEvent actionEvent) {
    }
}