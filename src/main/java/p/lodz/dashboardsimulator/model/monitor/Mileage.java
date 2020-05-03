package p.lodz.dashboardsimulator.model.monitor;

import p.lodz.dashboardsimulator.utils.Utils;

import java.util.List;
import java.util.stream.Collectors;

public class Mileage {

    private double totalMileage;
    private List<Double> resettableMileages;

    public Mileage(double totalMileage, List<Double> resettableMileages) {
        this.totalMileage = totalMileage;
        this.resettableMileages = resettableMileages;
    }

    public double getTotalMileage() {
        return totalMileage;
    }

    public List<Double> getResettableMileages() {
        return resettableMileages;
    }

    @Override
    public String toString() {

        return "Mileage{" +
                "total=" + Utils.round(totalMileage, 3) +
                ", reset=" + resettableMileages.stream().map(value -> Utils.round(value, 3)).collect(Collectors.toList()) +
                '}';
    }
}
