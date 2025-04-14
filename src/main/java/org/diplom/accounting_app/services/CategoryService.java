package org.diplom.accounting_app.services;

import io.ebean.DB;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.diplom.accounting_app.controllers.AddCategoryDialogController;
import org.diplom.accounting_app.controllers.CategoryDialogController;
import org.diplom.accounting_app.models.Category;
import org.diplom.accounting_app.models.CurrentUser;
import org.diplom.accounting_app.models.User;

import java.io.IOException;
import java.util.List;

public class CategoryService {

    private CategoryService categoryService;

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    public List<Category> getCategoriesForCurrentUser() {
        User currentUser = CurrentUser.getCurrentUser();
        if (currentUser == null) {
            return List.of();
        }

        return DB.find(Category.class)
                .where()
                .eq("user", currentUser)
                .findList();
    }

    public void addCategory(String name) {
        User currentUser = CurrentUser.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("Ошибка: пользователь не найден.");
        }

        boolean exists = DB.find(Category.class)
                .where()
                .eq("user.id", currentUser.getId())
                .eq("name", name)
                .exists();

        if (exists) {
            throw new IllegalArgumentException("Ошибка: категория с таким именем уже существует.");
        }

        Category category = new Category(currentUser, name);
        category.save();
    }

    public void deleteCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Ошибка: категория не найдена.");
        }

        User currentUser = CurrentUser.getCurrentUser();
        if (currentUser == null || !category.getUser().equals(currentUser)) {
            throw new SecurityException("Ошибка: нельзя удалить чужую категорию.");
        }

        category.delete();
    }

    /**
     * Открывает диалог управления категориями.
     */
    public void showCategoryDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/diplom/accounting_app/category-dialog.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Управление категориями");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(loader.load()));

            CategoryDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setCategoryService(this); // Передаем сервис
            controller.loadCategories();

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Открывает диалог добавления новой категории.
     */
    public void showAddCategoryDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/diplom/accounting_app/add-category-dialog.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Добавить категорию");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(loader.load()));

            AddCategoryDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setCategoryService(this); // Передаем сервис

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
