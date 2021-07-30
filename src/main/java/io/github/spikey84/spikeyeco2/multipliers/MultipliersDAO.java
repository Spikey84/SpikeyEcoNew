package io.github.spikey84.spikeyeco2.multipliers;

import com.google.common.collect.Lists;
import io.github.spikey84.spikeyeco2.utils.Utils;

import java.sql.*;
import java.util.List;
import java.util.UUID;

public class MultipliersDAO {
    public static List<Multiplier> getActiveMultipliers(Connection connection) {
        Statement statement = null;
        List<Multiplier> multipliers = Lists.newArrayList();

        try {
            Class.forName("org.postgresql.Driver");
            connection.setAutoCommit(false);

            String query = "SELECT uuid, timeleft, mult FROM active_multipliers;";

            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                UUID uuid = Utils.build(resultSet.getString("uuid"));
                long time = resultSet.getLong("timeleft");
                double mult = resultSet.getDouble("mult");
                multipliers.add(new Multiplier(uuid,mult, time));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return multipliers;
    }

    public static void addMultiplier(Connection connection, Multiplier multiplier) {
        PreparedStatement statement = null;

        try {
            Class.forName("org.postgresql.Driver");
            connection.setAutoCommit(false);

            String query = """
                    INSERT INTO active_multipliers (uuid, mult, timeleft)\
                    VALUES\
                    (?, ?, ?) ON DUPLICATE KEY UPDATE \
                    uuid=?, mult=?, timeleft=?;
                    """;
            statement = connection.prepareStatement(query);

            statement.setString(1, Utils.strip(multiplier.getUuid()));
            statement.setDouble(2, multiplier.getMultiplier());
            statement.setLong(3, multiplier.getTime());
            statement.setString(4, Utils.strip(multiplier.getUuid()));
            statement.setDouble(5, multiplier.getMultiplier());
            statement.setLong(6, multiplier.getTime());

            statement.execute();

            statement.close();
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeMultiplier(Connection connection, UUID uuid) {
        PreparedStatement statement = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection.setAutoCommit(false);

            String query = """
                    DELETE FROM active_multipliers WHERE uuid = ?;
                    """;
            statement = connection.prepareStatement(query);
            statement.setString(1, Utils.strip(uuid));
            statement.execute();

            statement.close();
            connection.commit();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
