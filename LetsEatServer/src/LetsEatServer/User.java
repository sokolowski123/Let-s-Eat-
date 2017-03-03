package LetsEatServer;

public class User {
	String dateOfBirth;
	String userId;
        String name;
    	String gender;
    	Match[] matches;
	String[][] chat;
    
    public User(String dateOfBirth, String userId, String name, String gender, Match[] matches, String[][] chat) {
    	this.dateOfBirth = dateOfBirth;
    	this.userId = userId;
        this.name = name;
        this.gender = gender;
        this.matches = matches;
	this.chat = chat;
    }
}
