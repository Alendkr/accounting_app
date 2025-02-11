package org.diplom.accounting_app.controllers;

import io.ebean.DB;
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
import org.diplom.accounting_app.models.CurrentUser;
import org.diplom.accounting_app.models.Expense;
import org.diplom.accounting_app.models.Receipt;
import org.diplom.accounting_app.models.User;

import java.io.IOException;
import java.util.List;

public class MenuController {
    @FXML
    private TableView<Expense> expensesTable;
    @FXML
    private TableView<Receipt> receiptsTable;
    @FXML
    private TableColumn<Expense, Integer> expenseAmountColumn;
    @FXML
    private TableColumn<Expense, String> expenseDateColumn;
    @FXML
    private TableColumn<Receipt, Integer> receiptAmountColumn;
    @FXML
    private TableColumn<Receipt, String> receiptDateColumn;
    @FXML
    private PieChart financeChart;
    @FXML
    private Button addButton;

    private final ObservableList<Expense> expenses = FXCollections.observableArrayList();
    private final ObservableList<Receipt> receipts = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTableColumns();
        loadTransactions();
        updatePieChart();
    }

    private void setupTableColumns() {
        expenseAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        expenseDateColumn.setCellValueFactory(new PropertyValueFactory<>("expenseDate"));
        receiptAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        receiptDateColumn.setCellValueFactory(new PropertyValueFactory<>("receiptDate"));

        expensesTable.setItems(expenses);
        receiptsTable.setItems(receipts);
    }

    public void loadTransactions() {
        if (CurrentUser.getCurrentUser() == null) {
            System.out.println("Ошибка: Текущий пользователь не найден.");
            return;
        }

        int userId = CurrentUser.getCurrentUser().getId();

        // Загружаем расходы
        List<Expense> expenses = DB.find(Expense.class)
                .where()
                .eq("user.id", userId)
                .findList();

        // Загружаем доходы
        List<Receipt> receipts = DB.find(Receipt.class)
                .where()
                .eq("user.id", userId)
                .findList();

        expensesTable.getItems().setAll(expenses);
        receiptsTable.getItems().setAll(receipts);
    }


    public void updatePieChart() {
        if (CurrentUser.getCurrentUser() == null) {
            System.out.println("Ошибка: Текущий пользователь не найден.");
            return;
        }

        int userId = CurrentUser.getCurrentUser().getId();

        // Получаем сумму всех расходов
        int totalExpenses = DB.find(Expense.class)
                .where()
                .eq("user.id", userId)
                .findList()
                .stream()
                .mapToInt(Expense::getAmount)
                .sum();

        // Получаем сумму всех доходов
        int totalReceipts = DB.find(Receipt.class)
                .where()
                .eq("user.id", userId)
                .findList()
                .stream()
                .mapToInt(Receipt::getAmount)
                .sum();

        financeChart.getData().clear();
        financeChart.getData().add(new PieChart.Data("Доходы", totalReceipts));
        financeChart.getData().add(new PieChart.Data("Расходы", totalExpenses));
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
