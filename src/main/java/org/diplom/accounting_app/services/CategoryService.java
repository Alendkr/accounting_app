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
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.List;

public class CategoryService {

    private CategoryDialogController categoryDialogController;


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

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void showAddCategoryDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/diplom/accounting_app/add-category-dialog.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Добавить категорию");
            dialogStage.setScene(new Scene(loader.load()));

            AddCategoryDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setCategoryService(this); // Передаем ссылку на CategoryService

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public List<Category> getCategoriesByUser(User user) {
        return DB.find(Category.class)
                .where()
                .eq("user", user)
                .findList();
    }


    public List<Category> getCategoriesForCurrentUser() {
        User currentUser = CurrentUser.getCurrentUser();
        if (currentUser == null) {
            return List.of(); // Если пользователь не найден, возвращаем пустой список
        }

        return DB.find(Category.class)
                .where()
                .eq("user", currentUser)
                .findList();
    }


    public void setCategoryDialogController(CategoryDialogController controller) {
        this.categoryDialogController = controller;
    }


    public void addCategory(String name) {
        User currentUser = CurrentUser.getCurrentUser();
        if (currentUser == null) {
            System.out.println("Ошибка: пользователь не найден.");
            return;
        }

        boolean exists = DB.find(Category.class)
                .where()
                .eq("user.id", currentUser.getId())
                .eq("name", name)
                .exists();

        if (exists) {
            System.out.println("Ошибка: категория с таким именем уже существует.");
            return;
        }

        Category category = new Category(currentUser, name);
        category.save();
        System.out.println("Категория добавлена: " + name);

        // Проверяем, вызывается ли этот код
        if (categoryDialogController != null) {
            System.out.println("Обновляем категории в диалоге...");
            categoryDialogController.loadCategories();
        } else {
            System.out.println("categoryDialogController == null!");
        }
    }




    public void deleteCategory(Category category) {
        try {
            User currentUser = validateCategoryOwner(category);
            category.delete();
            System.out.println("Категория удалена: " + category.getName());
        } catch (Exception e) {
            System.out.println("Ошибка при удалении категории: " + e.getMessage());
        }
    }


    public void updateCategory(Category category, String newName) {
        User currentUser = validateCategoryOwner(category);

        // Проверяем, нет ли уже категории с таким именем
        boolean exists = DB.find(Category.class)
                .where()
                .eq("user", currentUser)
                .eq("name", newName)
                .exists();

        if (exists) {
            throw new IllegalArgumentException("Категория с таким названием уже существует.");
        }

        category.setName(newName);
        category.update();
    }

    /**
     * Проверяет, принадлежит ли категория текущему пользователю.
     * @throws IllegalStateException если пользователь не найден.
     * @throws IllegalArgumentException если категория null или имя пустое.
     * @throws SecurityException если пользователь не владеет категорией.
     */
    private static @NotNull User validateCategoryOwner(Category category) {
        User currentUser = CurrentUser.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("Не удалось определить текущего пользователя.");
        }

        if (category == null || category.getName() == null || category.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Категория или её название не могут быть пустыми");
        }

        if (!category.getUser().equals(currentUser)) {
            throw new SecurityException("Вы не можете изменять или удалять чужую категорию.");
        }

        return currentUser;
    }

}
