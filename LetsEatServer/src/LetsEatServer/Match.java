package LetsEatServer;

public class Match {
	boolean isValid;
	boolean isBlocked;
	String user1;
	String user2;
	
	public Match(boolean isValid, boolean isBlocked, String user1, String user2) {
		this.isValid = isValid;
		this.isBlocked = isBlocked;
		this.user1 = user1;
		this.user2 = user2;
	}
}
