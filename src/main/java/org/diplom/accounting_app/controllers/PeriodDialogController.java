package org.diplom.accounting_app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class PeriodDialogController {
    private boolean periodSelected = false;

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
        transactionTypeChoiceBox.setValue("Все транзакции");
        // Обработка выбора периода
        periodChoiceBox.setOnAction(_ -> {
            String selectedPeriod = periodChoiceBox.getValue();
            boolean isCustomPeriod = "Свой период".equals(selectedPeriod);

            // Включаем/отключаем DatePicker
            startDatePicker.setDisable(!isCustomPeriod);
            endDatePicker.setDisable(!isCustomPeriod);
        });

        // Проверка дат
        startDatePicker.setOnAction(_ -> {
            LocalDate startDate = startDatePicker.getValue();
            if (startDate != null) {
                endDatePicker.setDayCellFactory(_ -> new DateCell() {
                    @Override
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        setDisable(empty || date.isBefore(startDate)); // Блокируем даты до startDate
                    }
                });
            }
        });

        // Обработка кнопки "Найти"
        findButton.setOnAction(_ -> handleFind());

        // Обработка кнопки "Отмена"
        cancelButton.setOnAction(_ -> closeDialog());
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

        periodSelected = true; // Устанавливаем флаг, что период выбран!

        System.out.println("Период: " + selectedPeriod);
        System.out.println("Тип транзакции: " + transactionType);
        if (startDate != null && endDate != null) {
            System.out.println("С " + startDate + " по " + endDate);
        }

        dialogStage.close();
    }

    public String getSelectedPeriod() {
        return periodChoiceBox.getValue();
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

    public boolean isPeriodSelected() {
        return periodSelected;
    }

    public LocalDate getStartDate() {
        return startDatePicker.getValue();
    }

    public LocalDate getEndDate() {
        return endDatePicker.getValue();
    }

}

