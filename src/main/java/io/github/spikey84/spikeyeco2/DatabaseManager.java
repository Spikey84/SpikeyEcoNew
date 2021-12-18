package io.github.spikey84.spikeyeco2;

import io.github.spikey84.spikeyeco2.utils.ChatUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class DatabaseManager {
    public static Connection getConnection() {
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:mysql://54.37.245.155:3306/s6896_eco", "u6896_t8SL1kYSV3", "ta77+^mjz!kKJxUCXVzUjZzM");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    public static void createMultiplierTable(Connection connection) {
        Statement statement = null;
        try {
            Class.forName("org.sqlite.JDBC");

            statement = connection.createStatement();

            String query = """
                    CREATE TABLE IF NOT EXISTS active_multipliers (\
                      uuid VARCHAR(36) NOT NULL,\
                      mult DECIMAL NOT NULL,\
                      timeleft bigint NOT NULL,\
                      PRIMARY KEY (uuid)\
                    );
                    """;
            statement.executeUpdate(query);
            statement.close();

            Statement statement1 = null;
            statement = connection.createStatement();

            String query1 = """
                    CREATE TABLE IF NOT EXISTS stored_multipliers (\
                                          id SERIAL,\
                                          uuid VARCHAR(36) NOT NULL,\
                                          mult DECIMAL NOT NULL,\
                                          timeleft bigint NOT NULL,\
                      					  PRIMARY KEY (id)\
                                        );
                    """;
            statement.executeUpdate(query1);
            statement.close();

            connection.close();

            ChatUtils.positiveConsole("Database Loaded.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
