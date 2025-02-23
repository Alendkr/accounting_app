package org.diplom.accounting_app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class PeriodDialogController {

    @FXML
    private ChoiceBox<String> periodChoiceBox;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private ChoiceBox<String> transactionTypeChoiceBox;

    @FXML
    private Button findButton;

    @FXML
    private Button cancelButton;

    private Stage dialogStage;

    @FXML
    public void initialize() {
        // Обработка выбора периода
        periodChoiceBox.setOnAction(event -> {
            String selectedPeriod = periodChoiceBox.getValue();
            boolean isCustomPeriod = "Свой период".equals(selectedPeriod);

            // Включаем/отключаем DatePicker
            startDatePicker.setDisable(!isCustomPeriod);
            endDatePicker.setDisable(!isCustomPeriod);
        });

        // Обработка кнопки "Найти"
        findButton.setOnAction(event -> handleFind());

        // Обработка кнопки "Отмена"
        cancelButton.setOnAction(event -> closeDialog());
    }

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    private void handleFind() {
        String selectedPeriod = periodChoiceBox.getValue();
        String transactionType = transactionTypeChoiceBox.getValue();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if ("Свой период".equals(selectedPeriod) && (startDate == null || endDate == null)) {
            showAlert("Ошибка", "Пожалуйста, выберите даты начала и окончания периода.");
            return;
        }

        System.out.println("Период: " + selectedPeriod);
        System.out.println("Тип транзакции: " + transactionType);
        if (startDate != null && endDate != null) {
            System.out.println("С " + startDate + " по " + endDate);
        }

        // Можно передать данные обратно или сразу загрузить таблицы
        dialogStage.close();
    }

    private void closeDialog() {
        dialogStage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

