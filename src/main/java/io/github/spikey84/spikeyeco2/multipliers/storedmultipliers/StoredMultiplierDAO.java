package io.github.spikey84.spikeyeco2.multipliers.storedmultipliers;

import com.google.common.collect.Lists;
import io.github.spikey84.spikeyeco2.utils.Utils;
import io.github.spikey84.spikeyeco2.multipliers.Multiplier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

public class StoredMultiplierDAO {
    public static List<Multiplier> getStoredMultipliers(Connection connection) {
        Statement statement = null;
        List<Multiplier> multipliers = Lists.newArrayList();

        try {
            Class.forName("org.sqlite.JDBC");
            connection.setAutoCommit(false);

            String query = "SELECT uuid, timeleft, mult, id FROM stored_multipliers;";

            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                UUID uuid = Utils.build(resultSet.getString("uuid"));
                long time = resultSet.getLong("timeleft");
                double mult = resultSet.getDouble("mult");
                Multiplier tmp = new Multiplier(uuid,mult, time);
                tmp.setID(resultSet.getInt("id"));
                multipliers.add(tmp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return multipliers;
    }

    public static void addStoredMultiplier(Connection connection, Multiplier multiplier) {
        PreparedStatement statement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            connection.setAutoCommit(false);

            String query = """
                    INSERT INTO stored_multipliers (uuid, mult, timeleft)\
                    VALUES\
                    (?, ?, ?);
                    """;
            statement = connection.prepareStatement(query);

            statement.setString(1, Utils.strip(multiplier.getUuid()));
            statement.setDouble(2, multiplier.getMultiplier());
            statement.setLong(3, multiplier.getTime());

            statement.execute();
            statement.close();

            query = """
                    SELECT * FROM stored_multipliers ORDER BY id DESC LIMIT 1;
                    """;
            statement = connection.prepareStatement(query);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            resultSet.next();
            multiplier.setID(resultSet.getInt(1));

            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeMultiplier(Connection connection, int id) {
        PreparedStatement statement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            connection.setAutoCommit(false);

            String query = """
                    DELETE FROM stored_multipliers WHERE id = ?;
                    """;
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.execute();

            statement.close();
            connection.commit();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
