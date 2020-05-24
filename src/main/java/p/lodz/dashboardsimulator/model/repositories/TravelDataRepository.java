package p.lodz.dashboardsimulator.model.repositories;

import io.reactivex.Observable;
import p.lodz.dashboardsimulator.model.monitor.statistics.TravelStatistics;

import java.util.List;

/**
 * Interacts with travel statistics database. Allows to add new travel statistics, delete old ones and receive history.
 */
public interface TravelDataRepository {

    enum Type {
        JDBC, SERIALIZED
    }

    /**
     * Adds passed travel statistics to the database.
     * @param travelStatistics statistics to be added to the database.
     * @return Observable to the result of a query. True indicates a successful query, and false unsuccessful.
     */
    Observable<Boolean> addTravelStatistics(TravelStatistics travelStatistics);

    /**
     * Receives all travel statistics from the database.
     * @return Observable to the result of a query.
     */
    Observable<List<SignedTravelStatistics>> getAllTravelStatistics();

    /**
     * Removes row with passed id from the database.
     * @param id Id of row to be removed.
     * @return Observable to the result of a query. True indicates a successful query, and false unsuccessful.
     */
    Observable<Boolean> removeTravelStatistics(int id);
}
