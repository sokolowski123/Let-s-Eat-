package LetsEatServer;

import java.util.List;

public class Search {

	public static double getDistance(List<Double> user1, List<Double> user2) {
		Location loc1 = new Location("", Double.parseDouble(user1.get(0).toString()), Double.parseDouble(user1.get(0).toString()));
		Location loc2 = new Location("", Double.parseDouble(user2.get(0).toString()), Double.parseDouble(user2.get(0).toString()));
		double distance = loc1.distanceTo(loc2);
		return distance;
	}
}
