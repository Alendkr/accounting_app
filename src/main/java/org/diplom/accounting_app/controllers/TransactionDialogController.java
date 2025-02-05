package org.diplom.accounting_app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.diplom.accounting_app.models.CurrentUser;
import org.diplom.accounting_app.models.Expense;
import org.diplom.accounting_app.models.Receipt;
import org.diplom.accounting_app.models.User;

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

        User currentUser = CurrentUser.getCurrentUser();
        if (currentUser == null) {
            showAlert("Ошибка", "Не удалось определить пользователя!");
            return;
        }

        try {
            if (type.equals("Расход")) {
                Expense expense = new Expense();
                expense.setUser(currentUser);
                expense.setAmount(amount);
                expense.setExpenseDate(date);
                expense.setDescription(description);
                expense.save();
            } else {
                Receipt receipt = new Receipt();
                receipt.setUser(currentUser);
                receipt.setAmount(amount);
                receipt.setReceiptDate(date);
                receipt.setDescription(description);
                receipt.save();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось сохранить данные!");
            return;
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
