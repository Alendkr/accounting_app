package org.diplom.accounting_app.services;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.diplom.accounting_app.controllers.PeriodDialogController;

import java.io.IOException;

public class PeriodService {

    public boolean showPeriodDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/diplom/accounting_app/period-dialog.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Выбор периода");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(loader.load()));

            PeriodDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


}
