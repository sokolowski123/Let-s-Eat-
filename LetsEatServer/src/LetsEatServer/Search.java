package LetsEatServer;

import java.util.List;

public class Search {

	public static double getDistance(List<Double> user1, List<Double> user2) {
		Location loc1 = new Location("", user1.get(0).doubleValue(), user1.get(1).doubleValue());
		Location loc2 = new Location("", user2.get(0).doubleValue(), user2.get(1).doubleValue());
		double distance = loc1.distanceTo(loc2);
		return distance;
	}
}
