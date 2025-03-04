package org.diplom.accounting_app.services;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.diplom.accounting_app.controllers.PeriodDialogController;

import java.io.IOException;
import java.time.LocalDate;

public class PeriodService {

    private LocalDate startDate;
    private LocalDate endDate;

    public boolean showPeriodDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/diplom/accounting_app/period-dialog.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Выбрать период");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(loader.load()));

            PeriodDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

            String selectedPeriod = controller.getSelectedPeriod();
            if (selectedPeriod != null) {
                calculatePeriod(selectedPeriod, controller.getStartDate(), controller.getEndDate());
                return true;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    private void calculatePeriod(String period, LocalDate start, LocalDate end) {
        switch (period) {
            case "За сегодня":
                startDate = LocalDate.now();
                endDate = LocalDate.now();
                break;
            case "За неделю":
                startDate = LocalDate.now().minusDays(7);
                endDate = LocalDate.now();
                break;
            case "За месяц":
                startDate = LocalDate.now().withDayOfMonth(1);
                endDate = LocalDate.now();
                break;
            case "За год":
                startDate = LocalDate.now().withDayOfYear(1);
                endDate = LocalDate.now();
                break;
            case "Свой период":
                startDate = start;
                endDate = end;
                break;
            default:
                startDate = null;
                endDate = null;
                break;
        }
    }


    public boolean isPeriodSelected() {
        return startDate != null && endDate != null;
    }


    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setPeriodDates(LocalDate start, LocalDate end) {
        this.startDate = start;
        this.endDate = end;
    }


}
