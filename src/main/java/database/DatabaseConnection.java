package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:C:/Users/alend/Desktop/diplom/accounting_app/src/main/java/database/diplom.db"; // Укажите путь к вашей базе данных

    public static Connection connect() {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(URL);
            System.out.println("Соединение с базой данных установлено.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
}

