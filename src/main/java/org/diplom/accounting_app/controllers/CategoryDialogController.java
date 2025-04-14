package org.diplom.accounting_app.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.diplom.accounting_app.models.Category;
import org.diplom.accounting_app.services.CategoryService;

import java.util.List;

public class CategoryDialogController {

    @FXML
    private ListView<Category> categoryListView;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button closeButton;

    private CategoryService categoryService;
    private final ObservableList<Category> categories = FXCollections.observableArrayList();
    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @FXML
    private void initialize() {
        categoryListView.setItems(categories);
    }

    public void loadCategories() {
        List<Category> categoryList = categoryService.getCategoriesForCurrentUser();
        categories.setAll(categoryList);
    }

    @FXML
    private void handleAddCategory() {
        categoryService.showAddCategoryDialog();
        loadCategories(); // Обновляем список после добавления
    }

    @FXML
    private void handleDeleteCategory() {
        Category selectedCategory = categoryListView.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            categoryService.deleteCategory(selectedCategory);
            loadCategories();
        }
    }

    @FXML
    private void handleClose() {
        dialogStage.close();
    }
}
