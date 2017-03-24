package cs307spring17team26.lets_eat_;

/******************************************************************************
 *  Compilation:  javac Location.java
 *  Execution:    java Location
 *
 *  Immutable data type for a named location: name and 
 *  (latitude, longitude).
 *
 *  % java LocationTest
 *  172.367 miles from
 *  PRINCETON_NJ (40.366633, 74.640832) to ITHACA_NY (42.443087, 76.488707)
 *
 ******************************************************************************/

public class Location { 
    private String name;
    private double longitude;
    private double latitude;   
   
    // create and initialize a point with given name and
    // (latitude, longitude) specified in degrees
    public Location(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude  = latitude;
        this.longitude = longitude;
    }

    // return distance between this location and that location
    // measured in statute miles
    public double distanceTo(Location that) {
        double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
        double lat1 = Math.toRadians(this.latitude);
        double lon1 = Math.toRadians(this.longitude);
        double lat2 = Math.toRadians(that.latitude);
        double lon2 = Math.toRadians(that.longitude);

        // great circle distance in radians, using law of cosines formula
        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                               + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        // each degree on a great circle of Earth is 60 nautical miles
        double nauticalMiles = 60 * Math.toDegrees(angle);
        double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
        return statuteMiles;
    }

    // return string representation of this point
    public String toString() {
        return name + " (" + latitude + ", " + longitude + ")";
    }

}
