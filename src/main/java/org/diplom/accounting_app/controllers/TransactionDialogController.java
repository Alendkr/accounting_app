package org.diplom.accounting_app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.diplom.accounting_app.services.TransactionService;

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

    private Stage dialogStage;
    private final TransactionService transactionService = new TransactionService();
    private boolean transactionSaved = false; // Флаг успешного сохранения

    @FXML
    public void initialize() {
//        typeChoice.getItems().addAll("Доход", "Расход");
        typeChoice.setValue("Доход");

        saveButton.setOnAction(event -> saveTransaction());
        cancelButton.setOnAction(event -> closeDialog());
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    private void saveTransaction() {
        String type = typeChoice.getValue();
        String amountText = amountField.getText();
        LocalDate date = datePicker.getValue();
        String description = descriptionField.getText();

        // Валидация данных
        if (type == null || amountText.isEmpty() || date == null) {
            showAlert("Ошибка", "Заполните все поля!");
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(amountText);
        } catch (NumberFormatException e) {
            showAlert("Ошибка", "Введите корректную сумму!");
            return;
        }

        // Передаём данные в `TransactionService`
        boolean success = transactionService.saveTransaction(type, amount, date, description);

        if (success) {
            transactionSaved = true;
            dialogStage.close();
        } else {
            showAlert("Ошибка", "Не удалось сохранить транзакцию!");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeDialog() {
        dialogStage.close();
    }

    public boolean isTransactionSaved() {
        return transactionSaved;
    }
}