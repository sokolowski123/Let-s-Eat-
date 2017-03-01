package LetsEatServer;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User {
    int dataOfBirth;
	String userId;
    String name;
    String gender;
    Match[] matches;
    
    public User(int dataOfBirth, String userId, String name, String gender, Match[] matches) {
    	this.dataOfBirth = dataOfBirth;
    	this.userId = userId;
        this.name = name;
        this.gender = gender;
        this.matches = matches;
    }
    
}
