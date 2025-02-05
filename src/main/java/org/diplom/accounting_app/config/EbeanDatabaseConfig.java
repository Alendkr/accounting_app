package org.diplom.accounting_app.config;

import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import io.ebean.datasource.DataSourceConfig;

public class EbeanDatabaseConfig {
    private static Database database;

    public static Database getDatabase() {
        if (database == null) {
            database = initializeDatabase();
        }
        return database;
    }

    private static Database initializeDatabase() {
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUsername("diplom");
        dataSourceConfig.setPassword("");
        dataSourceConfig.setUrl("jdbc:sqlite:database/diplom.db");
        dataSourceConfig.setDriver("org.sqlite.JDBC");

        DatabaseConfig config = new DatabaseConfig();
        config.setDataSourceConfig(dataSourceConfig);

        // Add your entity packages here
        config.setDefaultServer(true);
        config.addClass(org.diplom.accounting_app.models.User.class);

        return DatabaseFactory.create(config);
    }
}