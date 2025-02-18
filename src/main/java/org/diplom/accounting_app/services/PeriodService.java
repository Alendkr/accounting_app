package org.diplom.accounting_app.services;

import io.ebean.DB;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.diplom.accounting_app.controllers.TransactionDialogController;
import org.diplom.accounting_app.models.Expense;
import org.diplom.accounting_app.models.Receipt;
import org.diplom.accounting_app.models.User;
import org.diplom.accounting_app.models.CurrentUser;

import java.io.IOException;
import java.time.LocalDate;

public class PeriodService {

    public boolean showPeriodDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/diplom/accounting_app/period-dialog.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Поиск по дате");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(loader.load()));

            TransactionDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

            // Проверяем, была ли транзакция успешно сохранена
            return controller.isTransactionSaved();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


}
