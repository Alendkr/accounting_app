package org.diplom.accounting_app.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.diplom.accounting_app.services.CategoryService;

public class AddCategoryDialogController {

    @FXML
    private TextField categoryNameField;
    @FXML
    private Button addButton;
    @FXML
    private Button cancelButton;


    private Stage dialogStage;
    private CategoryService categoryService;


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }


    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    public void setCategoryDialogController(CategoryDialogController categoryDialogController) {
    }


    @FXML
    private void initialize() {
        addButton.setOnAction(event -> handleAddCategory());
        cancelButton.setOnAction(event -> dialogStage.close());
    }


    private void handleAddCategory() {
        String categoryName = categoryNameField.getText().trim();
        if (!categoryName.isEmpty()) {
            categoryService.addCategory(categoryName);
            dialogStage.close();
        } else {
            System.out.println("Ошибка: название категории не может быть пустым.");
        }
    }




    public void handleExitButton(ActionEvent actionEvent) {
        dialogStage.close();
    }

}

