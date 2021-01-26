package com.geotop.geotopproject.loader.geosplitter;

import com.geotop.geotopproject.loader.geosplitter.model.GeoPoint;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DistanceResolver {

    /**
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     *
     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
     * el2 End altitude in meters
     * @return Distance in Meters
     */
    public double calculateDistance(double lat1, double lat2, double lon1, double lon2) {
        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        distance = Math.pow(distance, 2);

        return Math.sqrt(distance);
    }

    public GeoPoint getMiddlePoint(GeoPoint upPoint, GeoPoint bottomPoint) {
        double lon2 = upPoint.getLongitude();
        double lon1 = bottomPoint.getLongitude();
        double lat2 = upPoint.getLatitude();
        double lat1 = bottomPoint.getLatitude();

        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        lon1 = Math.toRadians(lon1);

        double Bx = Math.cos(lat2) * Math.cos(dLon);
        double By = Math.cos(lat2) * Math.sin(dLon);
        double lat3 = Math.atan2(Math.sin(lat1) + Math.sin(lat2), Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By));
        double lon3 = lon1 + Math.atan2(By, Math.cos(lat1) + Bx);

        return new GeoPoint(Math.toDegrees(lat3), Math.toDegrees(lon3));
    }

    public GeoPoint getCentralGeoPoint(List<GeoPoint> geoCoordinates) {
        if (geoCoordinates.size() == 1) {
            return geoCoordinates.get(0);
        }

        double x = 0;
        double y = 0;
        double z = 0;

        for (GeoPoint geoCoordinate : geoCoordinates) {
            double latitude = geoCoordinate.getLatitude() * Math.PI / 180;
            double longitude = geoCoordinate.getLongitude() * Math.PI / 180;

            x += Math.cos(latitude) * Math.cos(longitude);
            y += Math.cos(latitude) * Math.sin(longitude);
            z += Math.sin(latitude);
        }
        int total = geoCoordinates.size();

        x = x / total;
        y = y / total;
        z = z / total;

        double centralLongitude = Math.atan2(y, x);
        double centralSquareRoot = Math.sqrt(x * x + y * y);
        double centralLatitude = Math.atan2(z, centralSquareRoot);

        return new GeoPoint(centralLatitude * 180 / Math.PI,centralLongitude * 180 / Math.PI);
    }

    public int getAreasCount(GeoPoint point1, GeoPoint point2, int radius) {
        return (int) calculateDistance(point1.getLatitude(), point2.getLatitude(), point1.getLongitude(), point2.getLongitude()) / (radius * 2);
    }
}
