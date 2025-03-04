package org.diplom.accounting_app.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.diplom.accounting_app.models.TransactionItem;
import org.diplom.accounting_app.services.FinanceService;
import org.diplom.accounting_app.services.TransactionService;
import org.diplom.accounting_app.services.PeriodService;

import java.time.LocalDate;
import java.util.List;

public class MenuController {

    private enum TableState {
        RECEIPTS, EXPENSES, ALL
    }
    private TableState currentTableState;

    @FXML
    private Label periodLabel;

    @FXML
    private Button resetFilterButton;

    @FXML
    private TableView<TransactionItem> transactionsTable;

    @FXML
    private TableColumn<TransactionItem, Integer> amountColumn;

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
        currentTableState = TableState.ALL;
        loadTransactions(transactionService.getAllTransactions());
        updateChart();
    }

    @FXML
    private void receiptButtonClick() {
        periodLabel.setText("Все доходы");
        currentTableState = TableState.RECEIPTS;
        loadTransactions(transactionService.getReceipts());
    }

    @FXML
    private void expenseButtonClick() {
        periodLabel.setText("Все расходы");
        currentTableState = TableState.EXPENSES;
        loadTransactions(transactionService.getExpenses());
    }

    @FXML
    private void historyButtonClick() {
        periodLabel.setText("Все транзакции");
        currentTableState = TableState.ALL;
        loadTransactions(transactionService.getAllTransactions());
    }

    private void loadTransactions(List<TransactionItem> transactions) {
        ObservableList<TransactionItem> observableTransactions = FXCollections.observableArrayList(transactions);
        transactionsTable.setItems(observableTransactions);
    }

    @FXML
    private void addButtonClick() {
        if (transactionService.showTransactionDialog()) {
            reloadCurrentTable();
            updateChart();
        }
    }

    private void loadTransactionsForPeriod(LocalDate startDate, LocalDate endDate) {
        List<TransactionItem> transactions = transactionService.getTransactionsForPeriod(startDate, endDate);
        transactionsTable.getItems().setAll(transactions);
        financeService.updatePieChartForPeriod(financeChart, transactions); // Обновляем диаграмму
    }



    @FXML
    private void periodButtonClick() {
        boolean periodAdded = periodService.showPeriodDialog();
        if (periodAdded) {
            LocalDate startDate = periodService.getStartDate();
            LocalDate endDate = periodService.getEndDate();

            if (startDate != null && endDate != null) {
                periodLabel.setText("С " + startDate + " по " + endDate);
                periodLabel.setVisible(true);
                resetFilterButton.setVisible(true);
                System.out.println("Выбранная дата начала: " + startDate);
                System.out.println("Выбранная дата окончания: " + endDate);

                // Загружаем транзакции за период
                loadTransactionsForPeriod(startDate, endDate);
            }
        }
    }




    @FXML
    private void resetFilterClick() {
        periodLabel.setText("Все транзакции");
        resetFilterButton.setVisible(false);
        List<TransactionItem> allTransactions = transactionService.getAllTransactions();

        currentTableState = TableState.ALL;
        transactionsTable.getItems().setAll(allTransactions);

        financeService.updatePieChart(financeChart); // Возвращаемся к полному обзору
    }


    private void reloadCurrentTable() {
        switch (currentTableState) {
            case RECEIPTS -> loadTransactions(transactionService.getReceipts());
            case EXPENSES -> loadTransactions(transactionService.getExpenses());
            case ALL -> loadTransactions(transactionService.getAllTransactions());
        }
    }

    private void updateChart() {
        financeService.updatePieChart(financeChart);
    }
}
