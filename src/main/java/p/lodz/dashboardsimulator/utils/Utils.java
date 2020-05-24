package p.lodz.dashboardsimulator.utils;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Contains static utility methods.
 */
public class Utils {

    public static double round(double value, int n) {
        double places = Math.pow(10.0, n);

        return Math.round(value * places) / places;
    }

    public static String formatDuration(long time) {

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

        return format.format(new Date(time));

    }
}
