package org.cccs.tfs.utils;

import org.cccs.tfs.domain.Location;
import org.cccs.tfs.domain.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * User: Craig Cook
 * Date: Jun 9, 2010
 * Time: 9:29:53 AM
 */
public final class RuntimeSupport {

    private static final Logger log = LoggerFactory.getLogger(RuntimeSupport.class);
    public static String IMAGE_DIRECTORY;

    static {
        Properties properties = new Properties();
        try {
            properties.load(RuntimeSupport.class.getResourceAsStream("/app.properties"));
            IMAGE_DIRECTORY = (String) properties.get("image.directory");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printPrincipals(List<Principal> values) {
        for (Principal p : values) {
            log.info(p.toString());
        }
    }

    public static boolean isNotEmpty(String s) {
        return s != null && !s.equals("");
    }

    public static boolean isEmpty(String s) {
        return !isNotEmpty(s);
    }

    public static List<Principal> convertSetToList(Set<Principal> principals) {
        return new ArrayList<Principal>(principals);
    }

    public static void populateDistances(Principal p) {
        for (Principal f: p.getFriends()) {
            double d = distanceBetween(p.getLocation(), f.getLocation());
            f.setDistance(d);
        }
    }

    public static double distanceBetween(Location loc1, Location loc2) {
        return distanceBetween(loc1.getLatitude(), loc1.getLongitude(), loc2.getLatitude(), loc2.getLongitude());
    }

    public static double distanceBetween(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 3958.75;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;

        int meterConversion = 1609;
        return dist * meterConversion;
    }
}
