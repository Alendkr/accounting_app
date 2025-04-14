package org.diplom.accounting_app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.diplom.accounting_app.services.CategoryService;

public class AddCategoryDialogController {

    @FXML
    private TextField categoryNameField;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    private CategoryService categoryService;
    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @FXML
    private void handleSave() {
        String categoryName = categoryNameField.getText().trim();
        if (!categoryName.isEmpty()) {
            categoryService.addCategory(categoryName);
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
}
