package org.diplom.accounting_app.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Pair;
import org.diplom.accounting_app.models.Expense;
import org.diplom.accounting_app.models.Receipt;
import org.diplom.accounting_app.services.FinanceService;
import org.diplom.accounting_app.services.TransactionService;

import java.util.List;
import java.util.Optional;

public class MenuController {
    @FXML
    private TableView<Expense> expensesTable;
    @FXML
    private TableView<Receipt> receiptsTable;
    @FXML
    private TableColumn<Receipt, Integer> receiptAmountColumn;
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
        loadTables();
        updateChart();
    }

    private void setupTableColumns() {
        expenseAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        expenseDateColumn.setCellValueFactory(new PropertyValueFactory<>("expenseDate"));
        receiptAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        receiptDateColumn.setCellValueFactory(new PropertyValueFactory<>("receiptDate"));
    }

    private void loadTables() {
        List<Expense> expenses = financeService.loadExpenses();
        List<Receipt> receipts = financeService.loadReceipts();

        expensesTable.setItems(FXCollections.observableArrayList(expenses));
        receiptsTable.setItems(FXCollections.observableArrayList(receipts));
    }

    private void updateChart() {
        Pair<Integer, Integer> financeData = financeService.getFinanceSummary();

        financeChart.getData().clear(); // Удаляем старые данные перед обновлением
        financeChart.getData().addAll(
                new PieChart.Data("Доход", financeData.getKey()),
                new PieChart.Data("Расход", financeData.getValue())
        );
    }

    @FXML
    private void addButtonClick() {
        Optional<TransactionService.TransactionData> data = transactionService.showTransactionDialog();
        data.ifPresent(transactionData -> {
            if (transactionService.saveTransaction(transactionData.type, transactionData.amount, transactionData.date, transactionData.description)) {
                loadTables();
                updateChart();
            }
        });
    }
}
