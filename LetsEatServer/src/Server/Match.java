package Server;

public class Match {
	boolean isValid;
	boolean isBlocked;
	User user1;
	User user2;
	
	public Match(boolean isValid, boolean isBlocked, User user1, User user2) {
		this.isValid = isValid;
		this.isBlocked = isBlocked;
		this.user1 = user1;
		this.user2 = user2;
	}
}
