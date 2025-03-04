package org.diplom.accounting_app.services;

import io.ebean.DB;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.diplom.accounting_app.controllers.TransactionDialogController;
import org.diplom.accounting_app.models.*;
import org.diplom.accounting_app.models.TransactionItem;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionService {

    public boolean showTransactionDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/diplom/accounting_app/transaction-dialog.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Добавить транзакцию");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(loader.load()));

            TransactionDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();
            return controller.isTransactionSaved();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean saveTransaction(String type, int amount, LocalDate date, String description) {
        User currentUser = CurrentUser.getCurrentUser();
        if (currentUser == null) {
            return false;
        }

        if ("Расход".equals(type)) {
            return saveExpense(amount, date, description, currentUser);
        } else {
            return saveReceipt(amount, date, description, currentUser);
        }
    }

    private boolean saveExpense(int amount, LocalDate date, String description, User user) {
        Expense expense = new Expense();
        expense.setUser(user);
        expense.setAmount(amount);
        expense.setExpenseDate(date);
        expense.setDescription(description);

        try {
            DB.save(expense);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean saveReceipt(int amount, LocalDate date, String description, User user) {
        Receipt receipt = new Receipt();
        receipt.setUser(user);
        receipt.setAmount(amount);
        receipt.setReceiptDate(date);
        receipt.setDescription(description);

        try {
            DB.save(receipt);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //  Получение доходов
    public List<TransactionItem> getReceipts() {
        List<TransactionItem> receipts = new ArrayList<>();
        List<Receipt> receiptList = DB.find(Receipt.class)
                .where().eq("UserID", CurrentUser.getCurrentUser().getId())
                .findList();

        for (Receipt receipt : receiptList) {
            receipts.add(new TransactionItem(receipt.getAmount(), receipt.getReceiptDate().toString()));
        }
        return receipts;
    }

    //  Получение расходов
    public List<TransactionItem> getExpenses() {
        List<TransactionItem> expenses = new ArrayList<>();
        List<Expense> expenseList = DB.find(Expense.class)
                .where().eq("UserID", CurrentUser.getCurrentUser().getId())
                .findList();

        for (Expense expense : expenseList) {
            expenses.add(new TransactionItem(expense.getAmount(), expense.getExpenseDate().toString()));
        }
        return expenses;
    }


    //  Получение всей истории (доходы и расходы)
    public List<TransactionItem> getAllTransactions() {
        List<TransactionItem> history = new ArrayList<>();

        List<Expense> expenses = DB.find(Expense.class)
                .where().eq("UserID", CurrentUser.getCurrentUser().getId())
                .findList();

        List<Receipt> receipts = DB.find(Receipt.class)
                .where().eq("UserID", CurrentUser.getCurrentUser().getId())
                .findList();

        for (Expense expense : expenses) {
            history.add(new TransactionItem(expense.getAmount() * -1, expense.getExpenseDate().toString()));
        }
        for (Receipt receipt : receipts) {
            history.add(new TransactionItem(receipt.getAmount(), receipt.getReceiptDate().toString()));
        }

        return history;

    }

    public List<TransactionItem> getTransactionsForPeriod(LocalDate startDate, LocalDate endDate) {
        List<TransactionItem> transactions = new ArrayList<>();

        List<Expense> expenses = DB.find(Expense.class)
                .where()
                .eq("UserID", CurrentUser.getCurrentUser().getId())
                .ge("strftime('%Y-%m-%d', expense_date)", startDate.toString())
                .le("strftime('%Y-%m-%d', expense_date)", endDate.toString())
                .findList();


        List<Receipt> receipts = DB.find(Receipt.class)
                .where()
                .eq("UserID", CurrentUser.getCurrentUser().getId())
                .ge("strftime('%Y-%m-%d', receipt_date)", startDate.toString())
                .le("strftime('%Y-%m-%d', receipt_date)", endDate.toString())
                .findList();

        for (Expense expense : expenses) {
            transactions.add(new TransactionItem(expense.getAmount() * -1, expense.getExpenseDate().toString()));
        }
        for (Receipt receipt : receipts) {
            transactions.add(new TransactionItem(receipt.getAmount(), receipt.getReceiptDate().toString()));
        }

        System.out.println("Загружено " + transactions.size() + " транзакций из базы данных.");
        return transactions;
    }

}
