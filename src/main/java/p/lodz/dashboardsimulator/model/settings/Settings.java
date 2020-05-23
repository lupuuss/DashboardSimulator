package p.lodz.dashboardsimulator.model.settings;

import p.lodz.dashboardsimulator.model.repositories.TravelDataRepository;

/**
 * Contains application settings
 */
public class Settings {

    private String databaseUser = "root";
    private String databasePassword = "root";

    private String databaseHost = "localhost:1433";
    private String databaseName = "dashboard";

    private long betweenEngineTicks = 50;
    private double accelerationConst = 20;
    private double maximumSpeed = 300;

    private TravelDataRepository.Type databaseType = TravelDataRepository.Type.JDBC;

    public String getDatabaseUser() {
        return databaseUser;
    }

    public void setDatabaseUser(String databaseUser) {
        this.databaseUser = databaseUser;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }

    public String getDatabaseHost() {
        return databaseHost;
    }

    public void setDatabaseHost(String databaseHost) {
        this.databaseHost = databaseHost;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public long getBetweenEngineTicks() {
        return betweenEngineTicks;
    }

    public void setBetweenEngineTicks(long betweenEngineTicks) {
        this.betweenEngineTicks = betweenEngineTicks;
    }

    public double getAccelerationConst() {
        return accelerationConst;
    }

    public void setAccelerationConst(double accelerationConst) {
        this.accelerationConst = accelerationConst;
    }

    public double getMaximumSpeed() {
        return maximumSpeed;
    }

    public void setMaximumSpeed(double maximumSpeed) {
        this.maximumSpeed = maximumSpeed;
    }

    public TravelDataRepository.Type getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(TravelDataRepository.Type databaseType) {
        this.databaseType = databaseType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Settings settings = (Settings) o;

        if (betweenEngineTicks != settings.betweenEngineTicks) return false;
        if (Double.compare(settings.accelerationConst, accelerationConst) != 0) return false;
        if (Double.compare(settings.maximumSpeed, maximumSpeed) != 0) return false;
        if (!databaseUser.equals(settings.databaseUser)) return false;
        if (!databasePassword.equals(settings.databasePassword)) return false;
        if (!databaseHost.equals(settings.databaseHost)) return false;
        if (!databaseName.equals(settings.databaseName)) return false;
        return databaseType == settings.databaseType;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = databaseUser.hashCode();
        result = 31 * result + databasePassword.hashCode();
        result = 31 * result + databaseHost.hashCode();
        result = 31 * result + databaseName.hashCode();
        result = 31 * result + (int) (betweenEngineTicks ^ (betweenEngineTicks >>> 32));
        temp = Double.doubleToLongBits(accelerationConst);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(maximumSpeed);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + databaseType.hashCode();
        return result;
    }
}
