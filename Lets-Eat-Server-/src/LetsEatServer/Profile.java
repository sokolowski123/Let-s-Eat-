package LetsEatServer;

import java.awt.Image;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Profile {
    int age;
    int prefMaxAge;
    int prefMinAge;
    String bio;
	String location;
	List <String> additionalInfo;
	Image profilePic;
	
	public Profile(int age, int prefMaxAge, int prefMinAge, String bio, String location, List <String> additionalInfo, Image profilePic) {
		this.age = age;
		this.prefMaxAge = prefMaxAge;
		this.bio = bio;
		this.location = location;
		this.additionalInfo = additionalInfo;
		this.profilePic = profilePic;
	}
}
