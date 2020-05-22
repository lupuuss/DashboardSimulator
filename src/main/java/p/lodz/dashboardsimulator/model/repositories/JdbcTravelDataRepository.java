package p.lodz.dashboardsimulator.model.repositories;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import p.lodz.dashboardsimulator.model.monitor.statistics.TravelStatistics;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Interacts with MS SQL database using JDBC.
 */
public class JdbcTravelDataRepository implements TravelDataRepository {

    private final String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private final String connectionUrl;
    private final String user;
    private final String password;

    private final String createTravelTableQuery = "IF OBJECT_ID('travels', 'U') IS NULL " +
            "CREATE TABLE travels(" +
            "id INT NOT NULL IDENTITY(1, 1) PRIMARY KEY," +
            "avgSpeed FLOAT NOT NULL," +
            "maxSpeed FLOAT NOT NULL," +
            "travelTime BIGINT NOT NULL," +
            "distance FLOAT NOT NULL," +
            "avgFuelConsumption FLOAT NOT NULL," +
            "travelDate DATETIME DEFAULT GETDATE()" +
            ");";

    private final String insertTravelQuery = "INSERT INTO travels(" +
            "avgSpeed," +
            "maxSpeed," +
            "travelTime," +
            "distance," +
            "avgFuelConsumption" +
            ") VALUES(?, ?, ?, ?, ?)";

    /**
     * Initialize database properties with given values.
     * @param user User name.
     * @param password Password for passed user.
     * @param databaseHost Ip address/domain where database server is hosted.
     * @param databaseName Name of database to be used.
     */
    public JdbcTravelDataRepository(String user, String password, String databaseHost, String databaseName) {
        this.user = user;
        this.password = password;
        connectionUrl = "jdbc:sqlserver://" + databaseHost + ";databaseName=" + databaseName + ";";
    }

    @FunctionalInterface
    private interface SqlThrowingFunction<T, R> {
        R apply(T arg) throws SQLException;
    }

    private <T> Observable<T> createConnectionConsumer(SqlThrowingFunction<Connection, T> consumer) {

        return Observable.create((emitter -> {

            try {
                Class.forName(driverName);
            } catch (ClassNotFoundException e) {
                emitter.onError(e);
                return;
            }

            try (Connection connection = DriverManager.getConnection(connectionUrl, user, password)) {

                emitter.onNext(consumer.apply(connection));
                emitter.onComplete();

            } catch (SQLException e) {
                emitter.onError(e);
            }
        }));
    }

    /**
     * Adds travel statistcs to the database.
     * @param travelStatistics statistics to be added to the database.
     * @return Observable state of a query. List of statistics is return if successful.
     */
    @Override
    public Observable<Boolean> addTravelStatistics(TravelStatistics travelStatistics) {
        return createConnectionConsumer(((connection) -> {

            try (PreparedStatement createTable = connection.prepareStatement(createTravelTableQuery);
                 PreparedStatement insertInto = connection.prepareStatement(insertTravelQuery)) {

                createTable.executeUpdate();

                insertInto.setDouble(1, travelStatistics.getAvgSpeed());
                insertInto.setDouble(2, travelStatistics.getMaxSpeed());
                insertInto.setLong(3, travelStatistics.getTravelTime());
                insertInto.setDouble(4, travelStatistics.getDistance());
                insertInto.setDouble(5, travelStatistics.getAvgFuelConsumption());
                return insertInto.executeUpdate() == 1;
            }
        }))
                .subscribeOn(Schedulers.single());
    }

    /**
     * Performs a query that returns all travel statistics from database.
     * @return Observable state of a query. List of statistics is return if successful.
     */
    @Override
    public Observable<List<SignedTravelStatistics>> getAllTravelStatistics() {
        return createConnectionConsumer((connection) -> {

            try(
                    Statement statement = connection.createStatement();
                    ResultSet result = statement.executeQuery(
                        "SELECT * FROM travels"
                    )
            ) {

                List<SignedTravelStatistics> travels = new ArrayList<>();

                while (result.next()) {

                    travels.add(
                            new SignedTravelStatistics(
                                    result.getInt(1),
                                    result.getDouble(2),
                                    result.getDouble(3),
                                    result.getLong(4),
                                    result.getDouble(5),
                                    result.getDouble(6),
                                    result.getTimestamp(7)
                            )
                    );
                }

                return travels;
            }
        })
                .subscribeOn(Schedulers.single());
    }

    /**
     * Removes a row with given id from database.
     * @param id Id of row to be removed.
     * @return Observable state of a query. True is emitted if successful.
     */
    @Override
    public Observable<Boolean> removeTravelStatistics(int id) {
        return createConnectionConsumer((connection) -> {

            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM travels WHERE id=" + id + ";")) {

                return statement.executeUpdate() == 1;
            }
        })
                .subscribeOn(Schedulers.single());
    }
}
