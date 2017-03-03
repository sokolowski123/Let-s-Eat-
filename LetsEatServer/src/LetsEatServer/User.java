package LetsEatServer;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User {
    String dataOfBirth;
    String userId;
    String name;
    String gender;
    Match[] matches;
    String[][] chatHist;
    
    public User(String dataOfBirth, String userId, String name, String gender, Match[] matches, String[][] chatHist) {
    	this.dataOfBirth = dataOfBirth;
    	this.userId = userId;
        this.name = name;
        this.gender = gender;
        this.matches = matches;
        this.chatHist = chatHist;
    }
    
}
