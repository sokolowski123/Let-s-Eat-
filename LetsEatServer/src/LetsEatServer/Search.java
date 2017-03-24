package LetsEatServer;

public class Search {

	public static double getDistance(double[] user1, double[] user2) {
	  Location loc1 = new Location("", user1[0],user1[1]);
		Location loc2 = new Location("", user2[0],user2[1]);
		double distance = loc1.distanceTo(loc2);
		return distance;
	}
}
