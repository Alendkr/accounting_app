package org.diplom.accounting_app;

import io.ebean.DB;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.diplom.accounting_app.config.EbeanDatabaseConfig;
import org.diplom.accounting_app.database.DatabaseConnection;
import org.diplom.accounting_app.models.Expense;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainApplication extends Application {

    private static Stage primaryStage; // Переменная для хранения основного окна приложения

    @Override
    public void start(Stage stage) throws IOException {
        DatabaseConnection.initializeDatabase();

        // Initialize Ebean
        EbeanDatabaseConfig.getDatabase();
        primaryStage = stage;
        Logger.getLogger("javafx.fxml").setLevel(Level.SEVERE);
        setRoot("login-view", "Авторизация"); // Устанавливаем начальный FXML и заголовок
        primaryStage.show();
    }

    // Метод для переключения сцен с заголовком
    public static void setRoot(String fxml, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(fxml + ".fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setScene(scene);
        primaryStage.setTitle(title); // Устанавливаем заголовок окна
    }

    public static void main(String[] args) {
        launch();
    }
}