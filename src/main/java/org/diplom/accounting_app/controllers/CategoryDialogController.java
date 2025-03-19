package org.diplom.accounting_app.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.diplom.accounting_app.models.Category;
import org.diplom.accounting_app.services.CategoryService;

import java.util.List;

public class CategoryDialogController {

    @FXML
    private ListView<Category> categoryListView;
    @FXML
    private Button deleteButton;
    @FXML
    private Button closeButton;

    private  CategoryService categoryService = new CategoryService();
    private final ObservableList<Category> categories = FXCollections.observableArrayList();
    private Stage dialogStage;
    private boolean isCategorySaved = false;


    @FXML
    private void initialize() {
        loadCategories();
        CategoryService categoryService = new CategoryService();
        categoryService.setCategoryDialogController(this);
        categoryListView.setItems(categories);
        deleteButton.disableProperty().bind(categoryListView.getSelectionModel().selectedItemProperty().isNull());
    }

    public void loadCategories() {
        List<Category> categoryList = categoryService.getCategoriesForCurrentUser();
        categories.setAll(categoryList);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }


    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
        categoryService.setCategoryDialogController(this); // Передаем ссылку
    }


    @FXML
    private void handleDeleteCategory() {
        Category selectedCategory = categoryListView.getSelectionModel().getSelectedItem();
        if (selectedCategory == null) {
            showAlert("Ошибка", "Выберите категорию для удаления.", Alert.AlertType.WARNING);
            return;
        }

        try {
            categoryService.deleteCategory(selectedCategory);
            loadCategories();
        } catch (Exception e) {
            showAlert("Ошибка", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleClose() {
        if (dialogStage != null) {
            dialogStage.close();
        }
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Возвращает true, если в диалоговом окне была добавлена категория.
     */
    public boolean isCategorySaved() {
        return isCategorySaved;
    }

    /**
     * Устанавливает флаг, если категория была сохранена.
     */
    public void setCategorySaved(boolean categorySaved) {
        isCategorySaved = categorySaved;
    }


    public void showAddCategoryDialog() {
        categoryService.showAddCategoryDialog();
    }

}
