package p.lodz.dashboardsimulator.utils;

/**
 * Contains static utility methods.
 */
public class Utils {

    public static double round(double value, int n) {
        double places = Math.pow(10.0, n);

        return Math.round(value * places) / places;
    }
}
