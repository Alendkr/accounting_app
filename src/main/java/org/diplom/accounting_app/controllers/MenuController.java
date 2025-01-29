package org.diplom.accounting_app.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.diplom.accounting_app.database.DatabaseConnection;
import org.diplom.accounting_app.models.Transaction;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class MenuController {
    @FXML
    private TableView<Transaction> expensesTable;
    @FXML
    private TableView<Transaction> receiptsTable;
    @FXML
    private TableColumn<Transaction, Integer> expenseAmountColumn;
    @FXML
    private TableColumn<Transaction, String> expenseDateColumn;
    @FXML
    private TableColumn<Transaction, Integer> receiptAmountColumn;
    @FXML
    private TableColumn<Transaction, String> receiptDateColumn;
    @FXML
    private PieChart financeChart;
    @FXML
    private Button addButton;

    private final ObservableList<Transaction> expenses = FXCollections.observableArrayList();
    private final ObservableList<Transaction> receipts = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTableColumns();
        loadTransactions();
        updatePieChart();
    }

    private void setupTableColumns() {
        expenseAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        expenseDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        receiptAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        receiptDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        expensesTable.setItems(expenses);
        receiptsTable.setItems(receipts);
    }

    public void loadTransactions() {
        expenses.clear();
        receipts.clear();

        try (Connection conn = DatabaseConnection.connect()) {
            if (conn != null) {
                loadDataFromDB(conn, "Expenses", expenses);
                loadDataFromDB(conn, "Receipts", receipts);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadDataFromDB(Connection conn, String tableName, ObservableList<Transaction> list) throws SQLException {
        String query;
        boolean isExpense;

        if (tableName.equals("Expenses")) {
            query = "SELECT ID, UserID, Amount, expense_date, Descr FROM Expenses";
            isExpense = true;
        } else { // "Receipts"
            query = "SELECT ID, UserID, Amount, receipt_date, Descr FROM Receipts";
            isExpense = false;
        }

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Transaction(
                        rs.getInt("ID"),
                        rs.getInt("UserID"),
                        rs.getInt("Amount"),
                        LocalDate.parse(rs.getString(isExpense ? "expense_date" : "receipt_date")),
                        rs.getString("Descr"),
                        isExpense
                ));
            }
        }
    }


    public void updatePieChart() {
        int totalExpenses = expenses.stream().mapToInt(Transaction::getAmount).sum();
        int totalReceipts = receipts.stream().mapToInt(Transaction::getAmount).sum();

        financeChart.setData(FXCollections.observableArrayList(
                new PieChart.Data("Расходы", totalExpenses),
                new PieChart.Data("Доходы", totalReceipts)
        ));
    }

    @FXML
    private void openTransactionDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/diplom/accounting_app/transaction-dialog.fxml"));
            Parent root = loader.load();

            TransactionDialogController controller = loader.getController();
            controller.setMenuController(this); // Передаём текущий контроллер

            Stage stage = new Stage();
            stage.setTitle("Добавить операцию");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait(); // Ожидаем закрытия окна

            loadTransactions(); // Перезагружаем таблицы после добавления
            updatePieChart(); // Обновляем круговую диаграмму

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addButtonClick(ActionEvent actionEvent) {
        openTransactionDialog();
    }
}
