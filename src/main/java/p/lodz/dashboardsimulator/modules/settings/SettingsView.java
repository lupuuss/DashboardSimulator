package p.lodz.dashboardsimulator.modules.settings;

import p.lodz.dashboardsimulator.base.View;
import p.lodz.dashboardsimulator.model.repositories.TravelDataRepository;

public interface SettingsView extends View<SettingsPresenter> {

    String getDatabaseUser();

    void setDatabaseUser(String databaseUser);

    String getDatabasePassword();

    void setDatabasePassword(String databasePassword);

    String getDatabaseHost();

    void setDatabaseHost(String databaseHost);

    String getDatabaseName();

    void setDatabaseName(String databaseName);

    String getBetweenEngineTicks();

    void setBetweenEngineTicks(String betweenEngineTicks);

    String getAccelerationConst();

    void setAccelerationConst(String accelerationConst);

    String getMaximumSpeed();

    void setMaximumSpeed(String maximumSpeed);

    TravelDataRepository.Type getDatabaseType();

    void setDatabaseType(TravelDataRepository.Type databaseType);

}
