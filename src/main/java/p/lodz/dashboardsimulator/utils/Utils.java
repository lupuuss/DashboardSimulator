package p.lodz.dashboardsimulator.utils;

public class Utils {

    public static double round(double value, int n) {
        double places = Math.pow(10.0, n);

        return ((double) Math.round(value * places)) / places;
    }
}
