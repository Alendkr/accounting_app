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
import java.util.Optional;

public class TransactionService {

    public enum TransactionType {
        INCOME, EXPENSE
    }

    public static class TransactionData {
        public final TransactionType type;
        public final int amount;
        public final LocalDate date;
        public final String description;

        public TransactionData(TransactionType type, int amount, LocalDate date, String description) {
            this.type = type;
            this.amount = amount;
            this.date = date;
            this.description = description;
        }
    }

    public Optional<TransactionData> showTransactionDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/diplom/accounting_app/transaction-dialog.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Добавить транзакцию");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(loader.load()));

            TransactionDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

            return controller.getTransactionResult().map(data ->
                    new TransactionData(data.type, data.amount, data.date, data.description)
            );

        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public boolean saveTransaction(TransactionType type, int amount, LocalDate date, String description) {
        User currentUser = CurrentUser.getCurrentUser();
        if (currentUser == null) {
            return false;
        }

        if (type == TransactionType.EXPENSE) {
            return saveExpense(amount, date, description, currentUser);
        } else {
            return saveReceipt(amount, date, description, currentUser);
        }
    }

    private boolean saveExpense(int amount, LocalDate date, String description, User user) {
        try {
            Expense expense = new Expense();
            expense.setUser(user);
            expense.setAmount(amount);
            expense.setExpenseDate(date);
            expense.setDescription(description);
            DB.save(expense);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean saveReceipt(int amount, LocalDate date, String description, User user) {
        try {
            Receipt receipt = new Receipt();
            receipt.setUser(user);
            receipt.setAmount(amount);
            receipt.setReceiptDate(date);
            receipt.setDescription(description);
            DB.save(receipt);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
