package p.lodz.dashboardsimulator.modules.settings;

import p.lodz.dashboardsimulator.base.View;
import p.lodz.dashboardsimulator.model.repositories.TravelDataRepository;
import p.lodz.dashboardsimulator.modules.history.HistoryPresenter;

/**
 * Describes interactions with view in settings module.
  * Every {@link View} bounded to {@link HistoryPresenter} must implement this interface.
 */
public interface SettingsView extends View<SettingsPresenter> {

    /**
     * Returns database user name specified by the user.
     * @return Database user name specified by the user.
     */
    String getDatabaseUser();

    /**
     * Shows current database name.
     * @param databaseUser Database name to be shown.
     */
    void setDatabaseUser(String databaseUser);

    /**
     * Returns database password specified by the user.
     * @return Database password specified by the user.
     */
    String getDatabasePassword();

    /**
     * Shows current database password.
     * @param databasePassword Database password to be shown.
     */
    void setDatabasePassword(String databasePassword);

    /**
     * Returns database host name specified by the user.
     * @return Database host name specified by the user.
     */
    String getDatabaseHost();

    /**
     * Shows current database host.
     * @param databaseHost Database host name to be shown.
     */
    void setDatabaseHost(String databaseHost);

    /**
     * Returns database name specified by the user.
     * @return Database name specified by the user.
     */
    String getDatabaseName();

    /**
     * Shows current database name.
     * @param databaseName Database name to be shown.
     */
    void setDatabaseName(String databaseName);

    /**
     * Returns time between engine ticks in ms specified by the user.
     * @return Time between engine ticks in ms.
     */
    String getBetweenEngineTicks();

    /**
     * Shows current time between engine ticks in ms.
     * @param betweenEngineTicks Time between engine ticks in ms to be shown.
     */
    void setBetweenEngineTicks(String betweenEngineTicks);

    /**
     * Returns acceleration const in km/s^2 specified by the user.
     * @return  Acceleration const in km/s^2.
     */
    String getAccelerationConst();

    /**
     * Shows current acceleration const in km/s^2.
     * @param accelerationConst Acceleration const in km/s^2 to be shown.
     */
    void setAccelerationConst(String accelerationConst);

    /**
     * Returns maximum speed in km/h specified by the user.
     * @return Maximum speed in km/h.
     */
    String getMaximumSpeed();

    /**
     * Shows current maximum speed in km/h.
     * @param maximumSpeed Maximum speed in km/h to be shown.
     */
    void setMaximumSpeed(String maximumSpeed);

    /**
     * Returns database type specified by the user.
     * @return Enum that indicates the type of database.
     */
    TravelDataRepository.Type getDatabaseType();

    /**
     * Shows current database type.
     * @param databaseType Database type to be shown.
     */
    void setDatabaseType(TravelDataRepository.Type databaseType);

}
