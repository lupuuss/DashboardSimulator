package p.lodz.dashboardsimulator.model.monitor.odometer;

import p.lodz.dashboardsimulator.utils.Utils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Typical data class. Contains mileage data. That includes total mileage and an unlimited amount of resettable mileages.
 */
public class Mileage {

    private double totalMileage;
    private List<Double> resettableMileages;

    /**
     * @param totalMileage Total mileage in km.
     * @param resettableMileages All resettable mileages in km as list.
     */
    public Mileage(double totalMileage, List<Double> resettableMileages) {
        this.totalMileage = totalMileage;
        this.resettableMileages = resettableMileages;
    }

    /**
     * Returns total mileage in km.
     * @return Total mileage in km.
     */
    public double getTotalMileage() {
        return totalMileage;
    }

    /**
     * Returns all resettable mileages as list.
     * @return All resettable mileages as list.
     */
    public List<Double> getResettableMileages() {
        return resettableMileages;
    }

    /**
     * Standard implementation of toString (json like).
     * @return {@link String} that contains all mileage data rounded to 3 decimal paces.
     */
    @Override
    public String toString() {

        return "Mileage{" +
                "total=" + Utils.round(totalMileage, 3) +
                ", reset=" + resettableMileages.stream().map(value -> Utils.round(value, 3)).collect(Collectors.toList()) +
                '}';
    }
}
