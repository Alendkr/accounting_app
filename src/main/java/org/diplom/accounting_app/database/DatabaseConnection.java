package org.diplom.accounting_app.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DB_NAME = "diplom.db";
    private static final String DB_DIR = "database";
    private static final String DB_PATH = System.getProperty("user.dir") + File.separator + DB_DIR + File.separator + DB_NAME;

    public static Connection connect() {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:" + DB_PATH;
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println("Ошибка подключения к БД: " + e.getMessage());
        }
        return conn;
    }

    public static void initializeDatabase() {
        File dbFile = new File(DB_PATH);
        if (!dbFile.exists()) {
            System.out.println("База данных не найдена, создаем новую...");
            try (Connection conn = connect()) {
                if (conn != null) {
                    DatabaseInitializer.createTables(conn);
                    System.out.println("База данных успешно создана и инициализирована.");
                }
            } catch (SQLException e) {
                System.out.println("Ошибка при создании базы данных: " + e.getMessage());
            }
        } else {
            System.out.println("База данных найдена, инициализация не требуется.");
        }
    }
}