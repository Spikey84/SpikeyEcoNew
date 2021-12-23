package io.github.spikey84.spikeyeco2;

import io.github.spikey84.spikeyeco2.utils.ChatUtils;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class DatabaseManager {
    private static File databaseFile;
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void initDatabase(Plugin plugin) {

        File databaseFolder = new File(plugin.getDataFolder(), "core.db");
        if (!databaseFolder.exists()) {
            try {
                databaseFolder.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        databaseFile = databaseFolder;
    }

    public static void createMultiplierTable(Connection connection) {
        Statement statement = null;
        try {
            Class.forName("org.sqlite.JDBC");

            statement = connection.createStatement();

            String query = """
                    CREATE TABLE IF NOT EXISTS active_multipliers (\
                      uuid VARCHAR(36) PRIMARY KEY,\
                      mult DECIMAL NOT NULL,\
                      timeleft bigint NOT NULL\
                    );
                    """;
            statement.executeUpdate(query);
            statement.close();

            Statement statement1 = null;
            statement = connection.createStatement();

            String query1 = """
                    CREATE TABLE IF NOT EXISTS stored_multipliers (\
                                          id INTEGER PRIMARY KEY AUTOINCREMENT,\
                                          uuid VARCHAR(36) NOT NULL,\
                                          mult DECIMAL NOT NULL,\
                                          timeleft bigint NOT NULL\
                                        );
                    """;
            statement.executeUpdate(query1);
            statement.close();

            ChatUtils.positiveConsole("Database Loaded.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
