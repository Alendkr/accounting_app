package org.diplom.accounting_app.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void createTables(Connection conn) {
        String createUsersTable = """
                CREATE TABLE IF NOT EXISTS Users (
                    ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    Name TEXT NOT NULL,
                    Login TEXT UNIQUE NOT NULL,
                    Password TEXT NOT NULL,
                    Money INTEGER DEFAULT 0
                );
                """;

        String createExpensesTable = """
                CREATE TABLE IF NOT EXISTS Expenses (
                    ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    UserID INTEGER NOT NULL,
                    Descr TEXT,
                    Amount INTEGER NOT NULL,
                    expense_date DATE NOT NULL,
                    FOREIGN KEY (UserID) REFERENCES Users(ID)
                );
                """;

        String createReceiptsTable = """
                CREATE TABLE IF NOT EXISTS Receipts (
                    ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    UserID INTEGER NOT NULL,
                    Descr TEXT,
                    Amount INTEGER NOT NULL,
                    receipt_date DATE NOT NULL,
                    FOREIGN KEY (UserID) REFERENCES Users(ID)
                );
                """;

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createUsersTable);
            stmt.execute(createExpensesTable);
            stmt.execute(createReceiptsTable);
            System.out.println("Все таблицы успешно созданы.");
        } catch (SQLException e) {
            System.out.println("Ошибка при создании таблиц: " + e.getMessage());
        }
    }
}