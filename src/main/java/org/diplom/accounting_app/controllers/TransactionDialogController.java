package org.diplom.accounting_app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.diplom.accounting_app.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class TransactionDialogController {

    @FXML
    private ChoiceBox<String> typeChoice;
    @FXML
    private TextField amountField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField descriptionField;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    private MenuController menuController;

    public void setMenuController(MenuController menuController) {
        this.menuController = menuController;
    }

    @FXML
    public void initialize() {
        typeChoice.getItems().addAll("Доход", "Расход");
        typeChoice.setValue("Доход"); // Значение по умолчанию

        saveButton.setOnAction(e -> saveTransaction());
        cancelButton.setOnAction(e -> closeDialog());
    }

    private void saveTransaction() {
        String type = typeChoice.getValue();
        int amount;
        LocalDate date = datePicker.getValue();
        String description = descriptionField.getText();

        try {
            amount = Integer.parseInt(amountField.getText());
        } catch (NumberFormatException e) {
            showAlert("Ошибка", "Введите корректную сумму!");
            return;
        }

        if (date == null) {
            showAlert("Ошибка", "Выберите дату!");
            return;
        }

        boolean isExpense = type.equals("Расход");
        String table = isExpense ? "Expenses" : "Receipts";
        String dateColumn = isExpense ? "expense_date" : "receipt_date";

        try (Connection conn = DatabaseConnection.connect()) {
            if (conn != null) {
                String query = "INSERT INTO " + table + " (UserID, Amount, " + dateColumn + ", Descr) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setInt(1, 1); // Пока ID пользователя = 1 (заменить на актуальный)
                    stmt.setInt(2, amount);
                    stmt.setString(3, date.toString());
                    stmt.setString(4, description);
                    stmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        menuController.loadTransactions(); // Обновляем таблицы
        menuController.updatePieChart(); // Обновляем диаграмму
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

