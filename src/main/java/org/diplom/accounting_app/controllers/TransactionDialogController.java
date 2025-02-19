package org.diplom.accounting_app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.diplom.accounting_app.services.TransactionService;
import org.diplom.accounting_app.services.TransactionService.TransactionData;

import java.time.LocalDate;
import java.util.Optional;

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
    private TransactionData transactionResult;

    @FXML
    public void initialize() {
        // Префаером ставим доход, юзер не тупой, а так стало красивей
        typeChoice.setValue("Доход");

        saveButton.setOnAction( _ -> saveTransaction());
        cancelButton.setOnAction( _ -> closeDialog());
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    private void saveTransaction() {
        String type = typeChoice.getValue();
        String amountText = amountField.getText();
        LocalDate date = datePicker.getValue();
        String description = descriptionField.getText();

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

        TransactionService.TransactionType transactionType =
                type.equals("Доход") ? TransactionService.TransactionType.INCOME : TransactionService.TransactionType.EXPENSE;

        transactionResult = new TransactionData(transactionType, amount, date, description);
        dialogStage.close();
    }

    public Optional<TransactionData> getTransactionResult() {
        return Optional.ofNullable(transactionResult);
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
}
