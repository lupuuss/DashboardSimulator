package p.lodz.dashboardsimulator.utils;

import java.io.File;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

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

    public static Optional<String> getFileExtension(File file) {

        String filename = file.getName();

        return Optional.of(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }
}
